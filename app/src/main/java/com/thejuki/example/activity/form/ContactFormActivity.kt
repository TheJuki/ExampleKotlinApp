package com.thejuki.example.activity.form

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.thejuki.example.R
import com.thejuki.example.activity.ScannerActivity
import com.thejuki.example.activity.detail.BaseDetailActivity
import com.thejuki.example.activity.detail.ContactDetailActivity
import com.thejuki.example.activity.form.ContactFormActivity.Tag.*
import com.thejuki.example.adapter.array.ContactAutoCompleteAdapter
import com.thejuki.example.adapter.array.ListItemAdapter
import com.thejuki.example.api.ApiClient
import com.thejuki.example.extension.simple
import com.thejuki.example.json.ContactJson
import com.thejuki.example.json.ContactLookupJson
import com.thejuki.example.json.ListItemJson
import com.thejuki.kformmaster.helper.*
import com.thejuki.kformmaster.listener.OnFormElementValueChangedListener
import com.thejuki.kformmaster.model.BaseFormElement
import com.thejuki.kformmaster.model.FormPickerDropDownElement
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import permissions.dispatcher.*

/**
 * Contact Form Activity
 *
 * Uses the KFormMaster library to add/edit a Contact.
 *
 * @author **TheJuki** ([GitHub](https://github.com/TheJuki))
 * @version 1.0
 */
@RuntimePermissions
class ContactFormActivity : BaseFormActivity<ContactJson>() {
    private val logTag = "ContactForm"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (mItem == null) {
            mItem = ContactJson(formAction = "Add")
        } else {
            if (mItem!!.id.isNullOrEmpty()) {
                mItem!!.formAction = "Add"
            } else {
                mItem!!.formAction = "Edit"
            }
        }

        if (mItem!!.formAction == "Add") {
            supportActionBar?.title = "New Contact"
        } else {
            supportActionBar?.title = "Contact: " + mItem!!.id.orEmpty()
        }

        setupForm()
    }

    @NeedsPermission(Manifest.permission.CAMERA)
    fun showCamera() {
        val intent = Intent(this, ScannerActivity::class.java)
        startActivity(intent)
    }

    @OnShowRationale(Manifest.permission.CAMERA)
    fun showRationaleForCamera(request: PermissionRequest) {
        AlertDialog.Builder(this)
                .setMessage(R.string.permission_camera_rationale)
                .setPositiveButton(R.string.button_allow, { _, _ -> request.proceed() })
                .setNegativeButton(R.string.button_deny, { _, _ -> request.cancel() })
                .show()
    }

    @OnPermissionDenied(Manifest.permission.CAMERA)
    fun onCameraDenied() {
        Toast.makeText(this, R.string.permission_camera_denied, Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("NeedOnRequestPermissionsResult")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // NOTE: delegate the permission handling to generated method
        // If this errors out, rebuild the project
        onRequestPermissionsResult(requestCode, grantResults)
    }

    private enum class Tag {
        FirstName,
        LastName,
        Team,
        Email,
        BusinessPhone,
        MobilePhone,
        Supervisor,
        Scan
    }

    override fun setupForm() {
        val listener = object : OnFormElementValueChangedListener {
            override fun onValueChanged(formElement: BaseFormElement<*>) {
                when (values()[formElement.tag]) {
                    Supervisor -> {
                        val supervisor = formElement.value as ContactLookupJson?
                        mItem!!.supervisorId = supervisor?.id
                        mItem!!.supervisorName = supervisor?.value
                    }
                    FirstName -> {
                        val value = formElement.value as String
                        mItem!!.firstName = value
                    }
                    LastName -> {
                        val value = formElement.value as String
                        mItem!!.lastName = value
                    }
                    Team -> {
                        val team = formElement.value as ListItemJson
                        mItem!!.teamId = team.id.orEmpty()
                        mItem!!.teamName = team.name
                    }
                    Email -> {
                        val value = formElement.value as String
                        mItem!!.emailAddress = value
                    }
                    BusinessPhone -> {
                        val value = formElement.value as String
                        mItem!!.businessPhone = value
                    }
                    MobilePhone -> {
                        val value = formElement.value as String
                        mItem!!.mobilePhone = value
                    }
                    Scan -> {
                        // If this errors out, rebuild the project to generate the method
                        showCameraWithPermissionCheck()
                    }
                }
            }
        }

        val formBuildHelper = form(binding.formRecyclerView, listener) {
            text(FirstName.ordinal) {
                title = getString(R.string.lbl_firstName)
                value = mItem!!.firstName.orEmpty()
            }
            text(LastName.ordinal) {
                title = getString(R.string.lbl_LastName)
                value = mItem!!.lastName.orEmpty()
            }
            dropDown<ListItemJson>(Team.ordinal) {
                title = getString(R.string.lbl_team)
                value = ListItemJson(id = mItem!!.teamId, name = mItem!!.teamName)
                dialogTitle = getString(R.string.lbl_team_select)
                arrayAdapter = ListItemAdapter(this@ContactFormActivity,
                        android.R.layout.simple_list_item_1)
            }
            email(Email.ordinal) {
                title = getString(R.string.lbl_emailAddress)
                value = mItem!!.emailAddress.orEmpty()
            }
            phone(BusinessPhone.ordinal) {
                title = getString(R.string.lbl_businessPhone)
                value = mItem!!.businessPhone.orEmpty()
            }
            phone(MobilePhone.ordinal) {
                title = getString(R.string.lbl_mobilePhone)
                value = mItem!!.mobilePhone.orEmpty()
            }
            autoComplete<ContactLookupJson>(Supervisor.ordinal) {
                title = getString(R.string.lbl_supervisor)
                value = ContactLookupJson(id = mItem!!.supervisorId, value = mItem!!.supervisorName, label = mItem!!.supervisorName)
                dropdownWidth = ViewGroup.LayoutParams.MATCH_PARENT
                arrayAdapter = ContactAutoCompleteAdapter(this@ContactFormActivity,
                        android.R.layout.simple_list_item_1)
            }
            header { title = getString(R.string.scan) }
            button(Scan.ordinal)
            {
                value = getString(R.string.scanner)
            }
        }

        fillTeamArrayAdapter((formBuildHelper.getFormElement<FormPickerDropDownElement<ListItemJson>>(Team.ordinal)).arrayAdapter as ListItemAdapter)
    }

    private fun fillTeamArrayAdapter(arrayAdapter: ListItemAdapter) {
        disposable = ApiClient.getInstance(this).getTeams()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            arrayAdapter.loadItems(result)
                        },
                        { error ->
                            Log.e(logTag, error.message ?: "")
                        }
                )
    }

    override fun save() {
        showProgressBar()

        if (validateForm()) {

            val simpleAlert = AlertDialog.Builder(this).create()

            disposable = ApiClient.getInstance(this).postContact(mItem!!)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            { result ->
                                if (!result.id.isNullOrEmpty()) {
                                    Toast.makeText(this, "Contact saved",
                                            Toast.LENGTH_SHORT)
                                            .show()
                                    if (mItem!!.formAction == "Add") {
                                        val intent = Intent(this, ContactDetailActivity::class.java).apply {
                                            putExtra(BaseDetailActivity.ARG_ITEM_ID, result.id!!)
                                        }
                                        startActivity(intent)
                                        finish()
                                    } else {
                                        val intent = Intent(this, ContactDetailActivity::class.java).apply {
                                            putExtra(BaseDetailActivity.ARG_ITEM_ID, mItem!!.id!!)
                                        }
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                        startActivity(intent)
                                        finish()
                                    }
                                } else {
                                    hideProgressBar()
                                    simpleAlert.simple(R.string.verification_error_title, R.string.verification_error_description)
                                }
                            },
                            { error ->
                                hideProgressBar()
                                Log.e(logTag, error.message ?: "")
                                simpleAlert.simple(R.string.form_error_title, R.string.form_error_description)
                            }
                    )
        } else {
            hideProgressBar()
        }
    }

    override fun validateForm(): Boolean {
        val simpleAlert = AlertDialog.Builder(this).create()
        var isValid = true

        //Validate the form
        if (isValid && mItem!!.firstName.isNullOrBlank()) {
            simpleAlert.simple(R.string.needed_first_name_error_title, R.string.needed_first_name_error_description)
            isValid = false
        }
        if (isValid && mItem!!.lastName.isNullOrBlank()) {
            simpleAlert.simple(R.string.needed_last_name_error_title, R.string.needed_last_name_error_description)
            isValid = false
        }
        if (isValid && mItem!!.businessPhone.isNullOrBlank()) {
            simpleAlert.simple(R.string.needed_business_phone_error_title, R.string.needed_business_phone_error_description)
            isValid = false
        }

        return isValid
    }
}
