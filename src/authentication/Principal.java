package authentication;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class Principal {
    public static void main(String[] args) throws InvalidKeyException, NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, UnsupportedEncodingException {
        Pessoa bob =  new Pessoa("bob", "bolabolabolabola");
        Pessoa alice = new Pessoa("alice", "patopatopatopato");

        KDC kdc  = new KDC(bob, alice);
        
        //Identificador
        String p1 = bob.getId();
        //Identificador cifrado na k_bob
        byte[] p2 = AES.cifra(bob.getId(), bob.getChaveMestre());
        //Alice cifrado na k_bob
        byte[] p3 = AES.cifra(alice.getId(), bob.getChaveMestre());

        kdc.gerarChaveSessao(p1, p2, p3);

        kdc.getKsCifradoBob();
        kdc.getKsCifradoAlice();

        kdc.bobKs();
		kdc.aliceKs();

		kdc.nonceDecifradoBobVerificadoAlice();
    }
}
