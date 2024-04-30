SET AUTOCOMMIT OFF;

SET TRANSACTION ISOLATION LEVEL READ COMMITTED;

UPDATE CUENTAS
SET saldo = saldo + 1000000
WHERE id = 141;

INSERT INTO Operaciones_Cuentas (id, tipo_operacion, fecha, cuenta_salida, monto_operacion, cliente, punto_atencion, cuenta_llegada)
VALUES (79, 'Consignacion', '25/04/24', 122, 1000000, 19, 67, 141);

UPDATE CUENTAS
SET saldo = saldo - 50000
WHERE id = 122;

INSERT INTO Operaciones_Cuentas (id, tipo_operacion, fecha, cuenta_salida, monto_operacion, cliente, punto_atencion, cuenta_llegada)
VALUES (80, 'Retiro', '25/04/24', 122, 50000, 19, 67, 100);

COMMIT;

SELECT saldo FROM CUENTAS WHERE id = 141;
SELECT saldo FROM CUENTAS WHERE id = 122;