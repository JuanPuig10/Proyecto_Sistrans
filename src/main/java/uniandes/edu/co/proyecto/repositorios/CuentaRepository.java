package uniandes.edu.co.proyecto.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import uniandes.edu.co.proyecto.modelo.Cuenta;

import java.sql.Date;
import java.util.Collection;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import jakarta.transaction.Transactional;

public interface CuentaRepository extends JpaRepository<Cuenta, Integer> {
    
    @Query(value = "SELECT * FROM cuentas", nativeQuery = true)
    Collection<Cuenta> darCuentas();

    @Query(value = "SELECT * FROM cuentas WHERE id=:id", nativeQuery = true)
    Cuenta darCuenta(@Param("id") long  id);
    
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO cuentas (id, numero_cuenta, estado,saldo, tipo,cliente, ultima_transaccion, gerente_oficina_creador,fecha_creacion) VALUES(proyecto_sequence.nextval, :numero_cuenta, :estado, :saldo, :tipo, :cliente, :ultima_transaccion, :gerente_oficina_creador, :fecha_creacion) ", nativeQuery = true)
    void insertarCuenta(@Param("numero_cuenta") String numero_cuenta,
        @Param("estado") String estado,
        @Param("saldo") Float saldo,
        @Param("tipo") String tipo,
        @Param("cliente") Integer cliente,
        @Param("ultima_transaccion") Date ultima_transaccion,
        @Param("gerente_oficina_creador") Integer gerente_oficina_creador,
        @Param("fecha_creacion") Date fecha_creacion);

    @Modifying
    @Transactional
    @Query(value = "UPDATE cuentas SET numero_cuenta=:numero_cuenta, estado =:estado, saldo = :saldo, tipo = :tipo, cliente = :cliente, ultima_transaccion = :ultima_transaccion, gerente_oficina_creador = :gerente_oficina_creador ,fecha_creacion= :fecha_creacion WHERE id = :id", nativeQuery = true)
    void actualizarCuenta(@Param("id") Integer  id,
        @Param("numero_cuenta") String numero_cuenta,
        @Param("estado") String estado,
        @Param("saldo") Float saldo,
        @Param("tipo") String tipo,
        @Param("cliente") Integer cliente,
        @Param("ultima_transaccion") Date ultima_transaccion,
        @Param("gerente_oficina_creador") Integer gerente_oficina_creador,
        @Param("fecha_creacion") Date fecha_creacion);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM cuentas WHERE id=:id", nativeQuery = true)
    void eliminarCuenta(@Param("id") long id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE cuentas SET saldo = :saldo", nativeQuery = true)
    void actualizarSaldoConsignar(@Param("id") Integer  id,
        @Param("saldo") Float saldo);

    @Modifying
    @Transactional
    @Query(value = "UPDATE cuentas SET saldo = :saldo", nativeQuery = true)
    void actualizarSaldoRetiro(@Param("id") Integer  id,
        @Param("saldo") Float saldo);

        
}
