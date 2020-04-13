import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Wallet {
    public HashMap < String, Trans_out > UTXOs = new HashMap < String, Trans_out > ();
    public PublicKey publicKey; //We can share public key(which acts as an address) with others to receive payment.
    public PrivateKey privateKey; //Users will keep their private key secret as it will be used to sign the transactions.

    public Wallet() {
        generateKeyPair();
    }

    public void generateKeyPair() {
        //We will use Elliptic curve Cryptography to generate our key private and public keys in a KeyPair.
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("ECDSA", "BC"); // Elliptic Curve Digital Signature Algorithm
            ECGenParameterSpec eS = new ECGenParameterSpec("prime192v1");
            SecureRandom secrand = SecureRandom.getInstance("SHA1PRNG");
            generator.initialize(eS, secrand);
            KeyPair kP = generator.generateKeyPair();

            publicKey = kP.getPublic();
            privateKey = kP.getPrivate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public float getBalance() {
        float totalval = 0;
        //Sum of all the unspent transaction outputs addressed to the user is the wallet balance
        for (Map.Entry < String, Trans_out > it: Mainchain.UTXOs.entrySet()) {
            Trans_out UTXO = it.getValue();
            if (UTXO.isMine(publicKey)) //Checking if the output belongs to the user then those coins will belong to the user
            {
                UTXOs.put(UTXO.id, UTXO); //appending value to the list of unspent transactions 
                totalval += UTXO.amount;
            }
        }
        return totalval;
    }

    public Transaction sendFunds(PublicKey _recipient,float value )
    {
       if(getBalance() < value)
        {
           System.out.println("#Not Enough funds to send transaction. Transaction Discarded.");
           return null;
       }

       ArrayList<Trans_inp> inputs = new ArrayList<Trans_inp>();

       float total = 0;
       for (Map.Entry < String, Trans_out > it: UTXOs.entrySet()) {
           Trans_out UTXO = it.getValue();
           total += UTXO.amount;
           inputs.add(new Trans_inp(UTXO.id));
           if (total > value)
               break;
       }

       Transaction newTransaction = new Transaction(publicKey, _recipient , value, inputs);
       newTransaction.sign_Creator(privateKey);

       for(Trans_inp input: inputs){
           UTXOs.remove(input.trans_out_id);
       }
       return newTransaction;
   }

}
