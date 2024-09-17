package pe.edu.upeu.calcfx.servicio;

import pe.edu.upeu.calcfx.modelo.CalcTO;

import java.util.List;

public interface CalcServiceI {
    public void guardarResultados(CalcTO to);

    public List<CalcTO> obtenerResultado();

    public void actualizarResultado(CalcTO to, int index);
    public void eliminarResultados(int index);
}



