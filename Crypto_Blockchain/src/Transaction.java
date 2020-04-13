import java.util.ArrayList;
import java.security.PrivateKey;
import java.security.PublicKey;

public class Transaction 
{
    public String trans_id;
    public PublicKey donor;//donor of amount
    public double amount;//amount transferred
    public byte[] sign;//proof owner of address is making this transaction and data is not modified
    public PublicKey receiver;//reciever of amount
    private static int seq = 0;
    public ArrayList<Trans_inp> inp = new ArrayList<Trans_inp>();//previous transactions showing the donor has amount to send
    public ArrayList<Trans_out> out = new ArrayList<Trans_out>();//shows amount received in the transaction.out are new transactions' inputs

    public Transaction(PublicKey from,PublicKey to,double amount,ArrayList<Trans_inp> inp) //constructor to initialize variables.
    {
        this.donor = from;
        this.amount = amount;
        this.receiver = to;
        this.inp = inp;
    }   

    public double getinpamount() //return total input
    {
        double tot = 0;
        for(Trans_inp i : inp) 
        {
            if(i.UTXO == null)
                continue;
            tot += i.UTXO.amount;
        }
        return tot;
    }

    public double getoutamount() //return total output
    { 
        double tot = 0;
        for(Trans_out o : out) {
            tot += o.amount;
        }
        return tot;
    }

    public void sign_Creator(PrivateKey priv_Key) //priv_key to create the sign.
    {
        String data = StringUtil.receiveStringFromKey(donor) + StringUtil.receiveStringFromKey(receiver) + Float.toString((float) amount);
        sign = StringUtil.toApplyECDSASig(priv_Key, data);
    }

    private String Hash_Calc()  //tranaction hash calculation
    {
        seq++;
        return StringUtil.generate256Sign(StringUtil.receiveStringFromKey(donor) + StringUtil.receiveStringFromKey(receiver)+Double.toString(amount)+seq);
    }   

    public boolean sign_verification()  //public key of donor to verify sign.
    {
        String data = StringUtil.receiveStringFromKey(donor) + StringUtil.receiveStringFromKey(receiver) + Double.toString(amount);
        return StringUtil.toVerifyECDSASig(donor, data, sign);
    }

    public boolean executeTransaction()
    {
        if(sign_verification() != true) //public key of donor used to check sign validity.
        {  
            System.out.println("#Transaction signature failed verification");
            return false;
        }

        for(Trans_inp i : inp)
            i.UTXO = Mainchain.UTXOs.get(i.trans_out_id);
        
        if(getinpamount() < Mainchain.minimumTransaction)
        {
            System.out.println("#Transaction input too small: " + getinpamount());
            return false;
        }

        double leftOver = getinpamount() - amount;
        trans_id = Hash_Calc();

        //change generated for donor
        out.add(new Trans_out(amount, trans_id, this.receiver));//transfer the amount to recipient
        out.add(new Trans_out(leftOver, trans_id, this.donor));//transfer the change to donor

        for(Trans_out o : out)
            Mainchain.UTXOs.put(o.id , o);//add out to unspent list

        for(Trans_inp i : inp)   //remove inp from UTXO lists as spent: 
        {
            if(i.UTXO == null)
                continue;//skip transaction if not found
            Mainchain.UTXOs.remove(i.UTXO.id);
        } 

        return true;
    }
}
