package com.thejuki.example.activity

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import com.google.zxing.BarcodeFormat
import com.google.zxing.Result
import com.thejuki.example.R
import com.thejuki.example.databinding.ActivityScannerBinding
import com.thejuki.example.extension.simple
import me.dm7.barcodescanner.zxing.ZXingScannerView

/**
 * Scanner Activity
 *
 * Provides a standalone Barcode/QR Scanner activity.
 * Copies the result to clipboard on scan and finishes.
 *
 * @author **TheJuki** ([GitHub](https://github.com/TheJuki))
 * @version 1.0
 */
class ScannerActivity : BaseActivity(), ZXingScannerView.ResultHandler {

    private lateinit var binding: ActivityScannerBinding
    private var mScannerView: ZXingScannerView? = null
    private var mFlash: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanner)

        binding = ActivityScannerBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.scanner)
        val contentFrame = binding.contentFrame
        mScannerView = ZXingScannerView(this)
        mScannerView?.setFormats(listOf(
                BarcodeFormat.UPC_E, BarcodeFormat.EAN_8, BarcodeFormat.EAN_13, BarcodeFormat.QR_CODE))
        binding.scannerFlashToggle.setOnClickListener {
            mFlash = !mFlash
            mScannerView?.flash = mFlash
        }
        contentFrame.addView(mScannerView)
    }

    override fun onOptionsItemSelected(item: MenuItem) =
            when (item.itemId) {
                android.R.id.home -> {
                    finish()
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }

    override fun onResume() {
        super.onResume()
        mScannerView?.setResultHandler(this)
        // You can optionally set aspect ratio tolerance level
        // that is used in calculating the optimal Camera preview size
        mScannerView?.setAspectTolerance(0.2f)
        mScannerView?.startCamera()
        mScannerView?.flash = mFlash
    }

    override fun onPause() {
        super.onPause()
        mScannerView?.stopCamera()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(FLASH_STATE, mFlash)
    }

    override fun handleResult(rawResult: Result) {
        val clipboard = this.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Copied Text", rawResult.text)
        clipboard.setPrimaryClip(clip)

        val simpleAlert = AlertDialog.Builder(this).create()
        simpleAlert.simple(R.string.scan_result_title, rawResult.text)
        finish()
    }

    companion object {
        private val FLASH_STATE = "FLASH_STATE"
    }
}
