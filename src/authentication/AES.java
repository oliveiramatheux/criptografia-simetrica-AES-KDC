package authentication;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class AES
{
    private static String ALGORITMO = "AES";

    public static byte[] cifra(String texto, String chave)
            throws IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException, InvalidKeyException
    {
        Key key =
                new SecretKeySpec(chave.getBytes(StandardCharsets.UTF_8), ALGORITMO);
        Cipher cifrador = Cipher.getInstance(ALGORITMO);
        cifrador.init(Cipher.ENCRYPT_MODE, key);
        byte[] textoCifrado = cifrador.doFinal(texto.getBytes());
        return textoCifrado;
    }

    public static String decifra(byte[] texto, String chave)
            throws IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException, InvalidKeyException
    {
        Key key =
                new SecretKeySpec(chave.getBytes(StandardCharsets.UTF_8), ALGORITMO);
        Cipher decifrador = Cipher.getInstance(ALGORITMO);
        decifrador.init(Cipher.DECRYPT_MODE, key);
        byte[] textoDecifrado = decifrador.doFinal(texto);
        return new String(textoDecifrado);
    }

    public static void Imprimir(String texto)
    {
        System.out.println(texto);
    }

    public static void Imprimir(byte[] texto)
    {
        System.out.println(new String(texto));
    }

    public static void main(String[] args) throws Exception {
        try
        {
           System.out.println("Run");
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}