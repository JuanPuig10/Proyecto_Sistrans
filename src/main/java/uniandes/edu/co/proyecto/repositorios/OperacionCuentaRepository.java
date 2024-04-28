package uniandes.edu.co.proyecto.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import uniandes.edu.co.proyecto.modelo.OperacionCuenta;

import java.sql.Date;
import java.util.Collection;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;

public interface OperacionCuentaRepository extends JpaRepository<OperacionCuenta, Integer> {


    @Query(value = "SELECT * FROM operaciones_cuentas", nativeQuery = true)
    Collection<OperacionCuenta> darOperacionesCuentas();

    @Query(value = "SELECT * FROM operaciones_cuentas WHERE id = :id", nativeQuery = true)
    OperacionCuenta darOperacioneCuenta(@Param("id") long id);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM operaciones_cuentas WHERE id = :id", nativeQuery = true)
    void eliminaOperacioneCuenta(@Param("id") long id);


    @Modifying
    @Transactional
    @Query(value = "INSERT INTO operaciones_cuentas (id, tipo_operacion, fecha, cuenta_salida,monto_operacion,cliente,punto_atencion,cuenta_llegada) VALUES(proyecto_sequence.nextval, :tipo_operacion, :fecha, :cuenta_salida, :monto_operacion, :cliente, :punto_atencion, :cuenta_llegada) ", nativeQuery = true)
    void insertarOperacioneCuenta(@Param("tipo_operacion") String tipo_operacion,
        @Param("fecha") Date fecha,
        @Param("cuenta_salida") Integer cuenta_salida,
        @Param("monto_operacion") Float monto_operacion,
        @Param("cliente") Integer cliente,
        @Param("punto_atencion") Integer punto_atencion,
        @Param("cuenta_llegada") Integer cuenta_llegada);

    @Modifying
    @Transactional
    @Query(value = "UPDATE operaciones_cuentas SET tipo_operacion = :tipo_operacion, fecha= :fecha, cuenta_salida= :cuenta_salida, monto_operacion= :monto_operacion, cliente= :cliente,punto_atencion = :punto_atencion, cuenta_llegada = :cuenta_llegada WHERE id = :id", nativeQuery = true)
    void actualizarOperacioneCuenta(@Param("id") long id,
        @Param("tipo_operacion") String tipo_operacion,
        @Param("fecha") Date fecha,
        @Param("cuenta_salida") Integer cuenta_salida,
        @Param("monto_operacion") Float monto_operacion,
        @Param("cliente") Integer cliente,
        @Param("punto_atencion") Integer punto_atencion,
        @Param("cuenta_llegada") Integer cuenta_llegada);


    @Query(value = "SELECT * FROM operaciones_cuentas WHERE fecha >= :fecha- 30 AND cuenta_salida = :numero_cuenta FOR UPDATE", nativeQuery = true)
    Collection<OperacionCuenta> consultaOpCuentaUltimoMes(@Param("fecha") Date fecha, @Param("numero_cuenta") Integer numero_cuenta);
}
