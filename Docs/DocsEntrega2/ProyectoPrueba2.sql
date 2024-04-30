SET TRANSACTION ISOLATION LEVEL READ COMMITTED;

UPDATE CUENTAS
SET saldo = saldo - 30000
WHERE id = 141;

INSERT INTO Operaciones_Cuentas (id, tipo_operacion, fecha, cuenta_salida, monto_operacion, cliente, punto_atencion, cuenta_llegada)
VALUES (81, 'Retiro', '25/04/24', 141, 30000, 19, 67, 100);

UPDATE CUENTAS
SET saldo = saldo + 5000
WHERE id = 122;

INSERT INTO Operaciones_Cuentas (id, tipo_operacion, fecha, cuenta_salida, monto_operacion, cliente, punto_atencion, cuenta_llegada)
VALUES (82, 'Consignacion', '25/04/24', 100, 5000, 19, 67, 122);

COMMIT;

SELECT saldo FROM CUENTAS WHERE id = 141;
SELECT saldo FROM CUENTAS WHERE id = 122;