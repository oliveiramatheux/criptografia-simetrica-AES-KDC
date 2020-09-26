package authentication;

public class Pessoa {
    private String id;
    private String chaveMestre;

    public Pessoa(String id, String chaveMestre){
        this.id = id;
        this.chaveMestre = chaveMestre;
    }

    public String getId(){
        return this.id;
    }

    public String getChaveMestre(){
        return this.chaveMestre;
    }
}
