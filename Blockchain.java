import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

class Block {
    private int index;
    private long timestamp;
    private String data;
    private String previousHash;
    private String hash;
    private int nonce;

    public Block(int index, long timestamp, String data, String previousHash) {
        this.index = index;
        this.timestamp = timestamp;
        this.data = data;
        this.previousHash = previousHash;
        this.nonce = 0;
        this.hash = calculateHash();
    }

    public String calculateHash() {
        String input = index + Long.toString(timestamp) + data + previousHash + nonce;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void mineBlock(int difficulty) {
        String target = new String(new char[difficulty]).replace('\0', '0');
        while (!hash.substring(0, difficulty).equals(target)) {
            nonce++;
            hash = calculateHash();
        }
        System.out.println("Block Mined!!! : " + hash);
    }

    // Getters for demonstration purposes
    public String getHash() {
        return hash;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public int getIndex() {
        return index;
    }

    public int getNonce() {
        return nonce;
    }

    public String getData() {
        return data;
    }
}

public class Blockchain {
    private List<Block> chain;

    public Blockchain() {
        chain = new ArrayList<>();
        // Add genesis block
        chain.add(createGenesisBlock());
    }

    private Block createGenesisBlock() {
        return new Block(0, System.currentTimeMillis(), "Genesis Block", "0");
    }
 
    public void addBlock(Block newBlock) {
        newBlock.mineBlock(4);  // Example difficulty
        chain.add(newBlock);
    }

    public void displayChain() {
        for (Block block : chain) {
            System.out.println("Block: " + block.getIndex());
            System.out.println("    Nonce: " + block.getNonce());
            System.out.println("    Hash: " + block.getHash());
            System.out.println("    PrevHash: " + block.getPreviousHash());
            System.out.println("    Data: " + block.getData());
        }
    }

    public static void main(String[] args) {
        Blockchain myBlockchain = new Blockchain();
        myBlockchain.addBlock(new Block(1, System.currentTimeMillis(), "Block 1", myBlockchain.chain.get(myBlockchain.chain.size() - 1).getHash()));
        myBlockchain.addBlock(new Block(2, System.currentTimeMillis(), "Block 2", myBlockchain.chain.get(myBlockchain.chain.size() - 1).getHash()));
        myBlockchain.displayChain();
    }
}
