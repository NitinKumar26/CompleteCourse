package `in`.completecourse

import android.content.Intent
import android.os.Bundle
import android.util.SparseArray
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.vision.barcode.Barcode
import info.androidhive.barcode.BarcodeReader
import info.androidhive.barcode.BarcodeReader.BarcodeReaderListener

class ScanActivity : AppCompatActivity(), BarcodeReaderListener {

    private var barcodeReader: BarcodeReader? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)
        title = "Scan QR Code"
        barcodeReader = supportFragmentManager.findFragmentById(R.id.barcode_scanner) as BarcodeReader?
    }

    override fun onScanned(barcode: Barcode) {
        // playing barcode reader beep sound
        barcodeReader!!.playBeep()
        // ticket details activity by passing barcode
        val intent = Intent(this@ScanActivity, MainActivity::class.java)
        intent.putExtra("url", barcode.displayValue)
        setResult(100, intent)
        finish()
    }

    override fun onScannedMultiple(barcodes: List<Barcode>) {}
    override fun onBitmapScanned(sparseArray: SparseArray<Barcode>) {}
    override fun onScanError(errorMessage: String) {}
    override fun onCameraPermissionDenied() {}
}