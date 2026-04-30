package Modelo;

import java.util.List;

public interface IComprar {
	
	
	VentaCafeteria comprarCafeteria(List<ItemMenu> items,String codigoDescuento, double propina);
	VentaJuego comprarJuegos(List<JuegoMesa> juegos, String codigoDescuento,InventarioVenta inventarioVenta);

}
