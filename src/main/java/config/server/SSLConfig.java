package config.server;

import infostructure.di.annotations.Configuration;
import infostructure.di.annotations.ManagedObject;
import infostructure.di.annotations.Value;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;

@Configuration
public class SSLConfig {

    @ManagedObject
    public SSLContext sslContext(
            @Value("storepass") String storepass,
            @Value("keypass") String keypass,
            @Value("key_store_file") String keyStoreLocation
    ){
        try {
            KeyStore ks = KeyStore.getInstance("JKS");
            File initialFile = new File(keyStoreLocation);
            InputStream targetStream = new FileInputStream(initialFile);
            ks.load(targetStream, storepass.toCharArray());

            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(ks, keypass.toCharArray());

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(kmf.getKeyManagers(), new TrustManager[]{}, null);

            return sslContext;

        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
