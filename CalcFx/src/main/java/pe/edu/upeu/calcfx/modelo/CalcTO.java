package pe.edu.upeu.calcfx.modelo;

public class CalcTO {
    String num1;
    String num2;
    char operdor;
    String resultado;


    public String getNum1() {
        return num1;
    }

    public void setNum1(String num1) {
        this.num1 = num1;
    }

    public String getNum2() {
        return num2;
    }

    public void setNum2(String num2) {
        this.num2 = num2;
    }

    public char getOperdor() {
        return operdor;
    }

    public void setOperdor(char operdor) {
        this.operdor = operdor;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    @Override
    public String toString() {
        return "CalcTO{" +
                "num1='" + num1 + '\'' +
                ", num2='" + num2 + '\'' +
                ", operdor=" + operdor +
                ", resultado='" + resultado + '\'' +
                '}';
    }
}
