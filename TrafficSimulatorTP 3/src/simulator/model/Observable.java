package simulator.model;

public interface Observable<T> {
	
	// Método para agregar un observador
	void addObserver(T o);
	
	// Método para eliminar un observador
	void removeObserver(T o);

}
