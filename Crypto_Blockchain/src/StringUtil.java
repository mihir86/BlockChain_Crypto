import java.security.*;
import java.util.ArrayList;
import java.util.Base64;

public class StringUtil

{

    //this applies SHA256 algorithm to a string and returns the result.It generates a 256 bit signature for a text.
    //A hash cannot be decrypted back to the original text.
    public static String generate256Sign(String input)
    {
        
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();
            for(int i=0;i<hash.length;i++)
            {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length()==1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }



    public static byte[] toApplyECDSASig(PrivateKey privateKey,String input)
    {
        //This appiles ECDSA Signature to the input and the sender's private key and returns the result as bytes.
        Signature dsa;
        byte[] output = new byte[0];
        try
        {
            dsa = Signature.getInstance("ECDSA","BC");
            dsa.initSign(privateKey);
            byte[] strByte = input.getBytes();
            dsa.update(strByte);
            byte[] realSig = dsa.sign();
            output = realSig;

        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
        return output;
    }

    public static boolean toVerifyECDSASig(PublicKey publicKey, String data, byte[] signature) 
    {

        //This will verify the signature using the public key and the string data.It returns True or False if the signature is valid
        //Only the public key of the reciepient will be able to sucessfully verify the transaction.
        try {
            Signature ecdsaVerify = Signature.getInstance("ECDSA", "BC");
            ecdsaVerify.initVerify(publicKey);
            ecdsaVerify.update(data.getBytes());
            return ecdsaVerify.verify(signature);
         }
        catch(Exception e)
         {
            throw new RuntimeException(e);
        }
    }

    public static String receiveStringFromKey(Key k)
    {
        return Base64.getEncoder().encodeToString(k.getEncoded());
    }

    public static String receiveMerkleRoot(ArrayList<Transaction> transactions) 
    {
        int count = transactions.size();
        ArrayList<String> previousTreeLayer = new ArrayList<String>();
        for(Transaction transaction : transactions)
         {
            previousTreeLayer.add(transaction.trans_id);
        }
        ArrayList<String> treeLayer = previousTreeLayer;
        while(count > 1) 
        {
            treeLayer = new ArrayList<String>();
            for(int i=1; i < previousTreeLayer.size(); i++) {
                treeLayer.add(generate256Sign(previousTreeLayer.get(i-1) + previousTreeLayer.get(i)));
            }
            count = treeLayer.size();
            previousTreeLayer = treeLayer;
        }
        String merkleRoot = (treeLayer.size() == 1) ? treeLayer.get(0) : "";
        return merkleRoot;
    }

    public static String getDifficultyString(int difficulty)
    {
        return new String(new char[difficulty]).replace('\0','0');
    }
}