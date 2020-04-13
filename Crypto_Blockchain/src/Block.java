import java.util.Date;
import java.util.ArrayList;

public class Block {
    //userID: stores user data such as Aadhar Number, PAN Number in the form of a String
    //epochTime: stores time elapsed since 1/1/1970
    //Nonce: a arbitrary number that is used once
    public String userID;
    public String blockHash;
    public String previousBlockHash;
    public long epochTime;
    public long nonce;
    public String merkleTreeRoot;
    public ArrayList<Transaction> transactionList = new ArrayList<Transaction>();

    //Constructor for the class Block
    public Block(String previousBlockHash, String userID)
    {
        this.userID = userID;
        this.epochTime = new Date().getTime();
        this.previousBlockHash = previousBlockHash;
        this.blockHash = this.computeBlockHash();
    }

    //Adds a transaction to transactionList of this Block
    public boolean addNewTransaction(Transaction newTransaction) {
        //process transaction if it is valid
        if(newTransaction == null) {
            System.out.println("Transaction is invalid!!!");
            return false;
        }
        else if(previousBlockHash.equals("0") == false && newTransaction.executeTransaction() == false) {
            System.out.println("Transaction not successfull!!!");
            return false;
        }
        //add the new transaction to transactionList
        transactionList.add(newTransaction);
        System.out.println("Transaction successful!!!");
        return true;
    }

    public void blockMine(int difficultyLevel) {
        //any node in the merkle tree is the hash of all it's children
        //hence the merkle root is the hash of the hashes of all transactions in the Block
        //adding or removing even one transaction changes the value of all parent's of the transaction
        //hence addding or removing a transaction also changes the merkle root hash
        //so we can use the merkle root as a value to verify the integrity of all transactions under it
        //in doing so, we do not need the body of the transactions
        merkleTreeRoot = StringUtil.receiveMerkleRoot(transactionList);

        //We create a string of required difficulty level
        String targetDifficultyString = StringUtil.getDifficultyString(difficultyLevel);

        //While blockHash has not reached targetDifficultyString
        //We keep increasing the nonce value and recompute blockHash
        while(blockHash.substring(0, difficultyLevel).equals(targetDifficultyString) == false) {
            nonce = nonce + 1;
            blockHash = computeBlockHash();
        }
        System.out.println("Block has been mined successfully!!!");
    }

    //Compute new hash based on current Block contents
    public String computeBlockHash() {
    	String temp = StringUtil.generate256Sign(previousBlockHash + Long.toString(epochTime) + Long.toString(nonce) + merkleTreeRoot);
        //return StringUtil.generate256Sign(previousBlockHash.concat(Long.toString(epochTime).concat(Long.toString(nonce).concat(merkleTreeRoot))));
    	return temp;
    }
}
