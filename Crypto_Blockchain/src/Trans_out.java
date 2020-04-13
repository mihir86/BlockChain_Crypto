import java.security.PublicKey;

public class Trans_out 
{
    public String id;// id for unique identification
    public double amount;//amount of owned coins  
    public String parent_trans_id;//transaction output created has this ID
    public PublicKey receiver;//the one who has been made owner of the coins

    public Trans_out(double amount, String parent_trans_id, PublicKey receiver)//Constructor to initialize variables
    {
        this.id = StringUtil.generate256Sign(StringUtil.receiveStringFromKey(receiver) + Double.toString(amount) + parent_trans_id);
        this.amount = amount;
        this.parent_trans_id = parent_trans_id;
        this.receiver = receiver;
    }

    public boolean isMine(PublicKey publicKey)//check ownership of coins
    {
        return (receiver == publicKey);
    }
}
