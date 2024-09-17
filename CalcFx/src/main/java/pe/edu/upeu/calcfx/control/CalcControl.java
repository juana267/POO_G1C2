package pe.edu.upeu.calcfx.control;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pe.edu.upeu.calcfx.modelo.CalcTO;
import pe.edu.upeu.calcfx.servicio.CalcServiceI;

import java.util.List;

@Component
public class CalcControl {
    @Autowired
    CalcServiceI serviceI;

    @FXML
    TextField txtResultado;

    @FXML
    TableView tableView;

    @FXML
    TableColumn<CalcTO, String> cVal1,cVal2,cResul;
    @FXML
    TableColumn<CalcTO, Character> cOper;
    @FXML
    TableColumn<CalcTO, Void> cOpc;

    private ObservableList<CalcTO> calcTOList;
    private int indexEdit=-1;


    @FXML
    public void accionButton(ActionEvent event) {
        System.out.println("Holas");
        Button button = (Button) event.getSource();
        switch (button.getId()) {
            case "btn7", "btn8", "btn9", "btn6", "btn5", "btn4", "btn3", "btn2", "btn1", "btn0" -> escribirNumeros(button.getText());
            case "btnSum", "btnMul", "btnRest", "btnDiv" -> operador(button.getText());
            case "btnIgual" -> calcularResultado();
            case "btnC", "AC" -> txtResultado.clear();  // Limpiar resultado con CE y AC
            case "btnRaiz" -> calcularRaiz();
            case "btnPotencia" -> calcularCuadrado();
            case "btnPoten" -> calcularPorcentaje(); // Agregar este método si lo implementas
            case "btnDv1" -> calcularInverso();
            case "btnBin" -> convertirBinario();
            case "btnPI" -> insertarPi();  // Insertar valor de Pi
            case "ON" -> encenderCalculadora(); // Agregar funcionalidad para ON si es necesario
        }
    }

    public void escribirNumeros(String valor) {
        txtResultado.appendText(valor);
    }

    public void operador(String valor) {
        txtResultado.appendText(" " + valor + " ");
    }

    public void calcularResultado() {
        String[] valores = txtResultado.getText().split(" ");
        double val1 = Double.parseDouble(valores[0]);
        double val2 = Double.parseDouble(valores[2]);
        switch (valores[1]) {
            case "+" -> txtResultado.setText(String.valueOf(val1 + val2));
            case "-" -> txtResultado.setText(String.valueOf(val1 - val2));
            case "/" -> txtResultado.setText(String.valueOf(val1 / val2));
            case "*" -> txtResultado.setText(String.valueOf(val1 * val2));
        }
        CalcTO to=new CalcTO();
        to.setNum1(String.valueOf(val1));
        to.setNum2(String.valueOf(val2));
        to.setOperdor(valores[1].charAt(0));
        to.setResultado(String.valueOf(txtResultado.getText()));
        if(indexEdit!=-1){
            serviceI.actualizarResultado(to, indexEdit);
        }else{
            serviceI.guardarResultados(to);
        }
        indexEdit=-1;
        listaOper();
    }

    public void calcularRaiz() {
        double val = Double.parseDouble(txtResultado.getText());
        txtResultado.setText(String.valueOf(Math.sqrt(val)));
    }

    public void calcularCuadrado() {
        double val = Double.parseDouble(txtResultado.getText());
        txtResultado.setText(String.valueOf(Math.pow(val, 2)));
    }

    // Implementación adicional para el botón de potencia, si es necesario
    public void calcularPorcentaje() {
        double val = Double.parseDouble(txtResultado.getText());
        txtResultado.setText(String.valueOf(val / 100));
    }

    public void calcularInverso() {
        double val = Double.parseDouble(txtResultado.getText());
        txtResultado.setText(String.valueOf(1 / val));
    }

    public void convertirBinario() {
        int val = Integer.parseInt(txtResultado.getText());
        txtResultado.setText(Integer.toBinaryString(val));
    }

    public void insertarPi() {
        txtResultado.setText(String.valueOf(Math.PI));
    }

    // Método para el botón ON si tiene algún comportamiento específico
    public void encenderCalculadora() {
        txtResultado.setText("0");
    }

    // Acción para editar una operación
    private void editOperCalc(CalcTO cal, int index) {
        System.out.println("Editing: " + cal.getNum1() + " Index:"+index);
        txtResultado.setText(cal.getNum1()+" "+cal.getOperdor()+""+cal.getNum2());
                indexEdit=index;
    }

    private void deleteOperCalc(CalcTO cal, int index) {
        System.out.println("Deleting: " + cal.getNum2());
        serviceI.eliminarResultados(index);
        listaOper();
        //tableView.getItems().remove(cal); // Elimina la operación delTableView
    }

    private void addActionButtonsToTable() {
        Callback<TableColumn<CalcTO, Void>, TableCell<CalcTO, Void>>
                cellFactory = param -> new TableCell<>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");
            {
                editButton.getStyleClass().setAll("btn", "btn-success");
                editButton.setOnAction(event -> {
                    CalcTO cal = getTableView().getItems().get(getIndex());
                    editOperCalc(cal, getIndex());
                });
                deleteButton.getStyleClass().setAll("btn", "btn-danger");
                deleteButton.setOnAction(event -> {
                    CalcTO cal = getTableView().getItems().get(getIndex());
                    deleteOperCalc(cal,getIndex());
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttons = new HBox(editButton, deleteButton);
                    buttons.setSpacing(10);
                    setGraphic(buttons);
                }
            }
        };
        cOpc.setCellFactory(cellFactory);
    }

    public void listaOper(){
        List<CalcTO> lista=serviceI.obtenerResultado();
        for (CalcTO to:lista) {
            System.out.println(to.toString());
        }
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        // Vincular columnas con propiedades de CalcTO
        cVal1.setCellValueFactory(new PropertyValueFactory<CalcTO,
                        String>("num1"));

        cVal1.setCellFactory(TextFieldTableCell.<CalcTO>forTableColumn());
        cVal2.setCellValueFactory(new PropertyValueFactory<CalcTO,
                String>("num2"));

        cVal2.setCellFactory(TextFieldTableCell.<CalcTO>forTableColumn());
        cOper.setCellValueFactory(new
                PropertyValueFactory<>("operador"));
        cOper.setCellFactory(ComboBoxTableCell.<CalcTO,
                Character>forTableColumn('+', '-', '/', '*'));
        cResul.setCellValueFactory(new PropertyValueFactory<CalcTO,
                String>("resultado"));

        cResul.setCellFactory(TextFieldTableCell.<CalcTO>forTableColumn());
        // Agregar botones de eliminar y modificar
        addActionButtonsToTable();
        calcTOList = FXCollections.observableArrayList(serviceI.obtenerResultado());
        // Asignar los datos al TableView
        AnchorPane.setLeftAnchor(tableView, 0.0);
        AnchorPane.setRightAnchor(tableView, 0.0);

        cOper.prefWidthProperty().bind(tableView.widthProperty().multiply(0.25)); // 25% del ancho total

        cResul.prefWidthProperty().bind(tableView.widthProperty().multiply(0.25)); // 25% del ancho total

        cOpc.prefWidthProperty().bind(tableView.widthProperty().multiply(0.25));
        tableView.setItems(calcTOList);
    }


}
