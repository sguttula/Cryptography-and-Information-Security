package security;

public class KeyPair {
    private PrivateKey kR;
    private PublicKey kU;
    
    public KeyPair(final PrivateKey kr, final PublicKey ku) {
        super();
        this.kR = kr;
        this.kU = ku;
    }
    
    public PrivateKey getPrivateKey() {
        return this.kR;
    }
    
    public PublicKey getPublicKey() {
        return this.kU;
    }
    
    public String toString() {
        return "KR=" + this.kR + System.getProperty("line.separator") + "KU=" + this.kU;
    }
}
