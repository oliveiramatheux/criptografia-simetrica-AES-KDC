package authentication;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class KDC {
    private Pessoa bob;
    private Pessoa alice;
    private byte[] ksCifradoBob;
    private byte[] ksCifradoAlice;
    private String chaveBobSessao;
    private String chaveAliceSessao;
    private int nonce;

    public KDC(Pessoa bob, Pessoa alice){
        this.bob = bob;
        this.alice = alice;
    }

    public void gerarChaveSessao(String id, byte[] idCifrado, byte[] destinatarioCifrado) throws BadPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, UnsupportedEncodingException, NoSuchPaddingException, InvalidKeyException {
        //Será que é realmente o usuario?
        String idDecifrado = AES.decifra(idCifrado, bob.getChaveMestre());

        if(id.equals(idDecifrado)){
            String destinatario = AES.decifra(destinatarioCifrado, bob.getChaveMestre());
            if(destinatario.equals(alice.getId())){
                //Usuario comunica com o outro
                String chaveSessao = getKs();
                this.ksCifradoBob = AES.cifra(chaveSessao, bob.getChaveMestre());
                this.ksCifradoAlice = AES.cifra(chaveSessao, alice.getChaveMestre());
            }
        }else{
            System.out.println("Usuário inválido!");
        }
    }

    public String getKs(){
        //Randon password 16 or 32 caracteres
        String chaveRandom = "";
        Random gerador = new Random();
        for (int i = 0; i < 16; i++) {
            chaveRandom += gerador.nextInt(9);
        }
        return chaveRandom;
    }

    public byte[] getKsCifradoBob(){
        return this.ksCifradoBob;
    }
    public byte[] getKsCifradoAlice(){
        return this.ksCifradoAlice;
    }

    public void bobKs() throws BadPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, UnsupportedEncodingException, NoSuchPaddingException, InvalidKeyException {
        //Bob recebe sua chave de sessao cifrada e decifra utilizando sua chave mestre

        chaveBobSessao = AES.decifra(getKsCifradoBob(), bob.getChaveMestre());
        System.out.println("Chave de Sessão de bob: " + chaveBobSessao);
    }

    public void aliceKs() throws BadPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, UnsupportedEncodingException, NoSuchPaddingException, InvalidKeyException {
        //Alice recebe sua chave de sessao cifrada e decifra utilizando sua chave mestre

        chaveAliceSessao = AES.decifra(getKsCifradoAlice(), alice.getChaveMestre());
        System.out.println("Chave de Sessão de alice: " + chaveAliceSessao);
    }

    public byte[] aliceNonce() throws BadPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, UnsupportedEncodingException, NoSuchPaddingException, InvalidKeyException {
		//gera um numero aleatorio entre 0 e 100 e cifra esse numero na chave de sessao de alice

        Random gerador = new Random();
		int nonceNumero = gerador.nextInt(1000);
		this.nonce = nonceNumero;

		byte[] aliceNonceCifrado = AES.cifra(Integer.toString(nonceNumero), chaveAliceSessao);
		return aliceNonceCifrado;
    }

    public String nonceBobDecifrado() throws BadPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, UnsupportedEncodingException, NoSuchPaddingException, InvalidKeyException {
        //Bob recebe o nonce cifrado da alice e utilizada sua chave de sessão para poder decifrar o nonce

        String nonceDecifrado = AES.decifra(aliceNonce(), chaveBobSessao);
        return nonceDecifrado;
    }

    public int nonceOperacao() throws BadPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, UnsupportedEncodingException, NoSuchPaddingException, InvalidKeyException {
        //pega o nonce decifrado de bob e adiciona o valor da operacao

        int resul = Integer.parseInt(nonceBobDecifrado()) + 10;
        return resul;
    }

    public int getNonce() {
        //retorna nonce cifrado
        return nonce;
    }
    
    public void nonceDecifradoBobVerificadoAlice() throws BadPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, UnsupportedEncodingException, NoSuchPaddingException, InvalidKeyException {
		//gera o valor do nonce de alice add com o valor da operacao pega o valor do nonceOperacao()
        // que bob pega o valor do nonce de alice soma com o valor da operacao para comparar o dois
        // resultados e verificar a autenticidade dos usuarios

		int nonceBob = nonceOperacao();
		int nonceAlice = getNonce();
		int nonceAliceSomado = nonceAlice + 10;

		System.out.println("Nonce: " + nonce);
		System.out.println("Nonce Alice: " + nonceAliceSomado);
        System.out.println("Nonce Bob: " + nonceBob);
        
		if (nonceBob == nonceAliceSomado) {
			System.out.println("Autenticação realizada, a conversa vai ser iniciada!");
		} else {
			System.out.println("Autenticação falhou!");
		}
    }
}
