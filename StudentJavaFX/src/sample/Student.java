package sample;

public class Student {

    public enum Smer {
        Informacione_tehnologije("Informacione tehnologije"), Softversko_inzenjerstvo("Softversko inzenjerstvo"), Racunarske_igre("Racunarske igre");

        private final String toString;

        private Smer(String toString) {
            this.toString = toString;
        }

        public String toString() {
            return toString;
        }
    }
    private String ime;
    private String prezime;
    private Smer smer;
    private boolean tradicionalno;

    public Student() {
    }

    public Student(String ime, String prezime, Smer smer, boolean tradicionalno) {
        this.ime = ime;
        this.prezime = prezime;
        this.smer = smer;
        this.tradicionalno = tradicionalno;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public Smer getSmer() {
        return smer;
    }

    public void setSmer(Smer smer) {
        this.smer = smer;
    }

    public boolean isTradicionalno() {
        return tradicionalno;
    }

    public void setTradicionalno(boolean tradicionalno) {
        this.tradicionalno = tradicionalno;
    }

    @Override
    public String toString() {
        return ime + " " + prezime + " " + smer.name() + " " + (tradicionalno ? "tradicionalno" : "internet");
    }

}