public class Trans_inp 
{
    public Trans_out UTXO; //UTXO has Unspent output of transaction, UTXO is bitcoin based model of transaction
    public String trans_out_id;
    public Trans_inp(String trans_out_id)//constructor to initialize variables  
    {
        this.trans_out_id = trans_out_id;
    }
}
