insert into clientes_cec.c_parametros values
('SUCURSALES_ADICIONAL','8,24');

Create table clientes_cec.c_porcentajeAdicional(
pa_ID int(2) not null,
pa_nombre varchar(20) not null,
pa_valor float not null  default 0,
pa_prioridad int(2) not null default 0,
PRIMARY KEY (pa_ID));

insert into clientes_cec.c_porcentajeAdicional values 
(1,'40%',.4,4),
(2,'30%',.3,8),
(3,'20%',.2,10);

alter table clientes_cec.d_integrantes_ciclo add Column ic_porcentajeAdicional int default 0;
alter table clientes_cec.d_integrantes_ciclo add Column ic_tipo_adi int default 0;

ALTER TABLE `clientes_cec`.`d_integrantes_ciclo` ADD COLUMN `ic_montoAdicional` DOUBLE(8,2) UNSIGNED default 0 AFTER `ic_porcentajeAdicional`,
 ADD COLUMN `ic_fechaDesembAdicional` default '1900-01-01 00:00:00' DATE AFTER `ic_montoAdicional`;

 alter table clientes_cec.d_ciclos_grupales add column ci_semadi int(1) default 0;

INSERT INTO clientes_cec.c_parametros (pa_cve_param, pa_valor) VALUES ('RUTA_DELETES_ODS', 'D:\\CLIENTES\\RespaldosSICAP\\');

INSERT INTO clientes_cec.c_parametros (pa_cve_param, pa_valor) VALUES ('SUCURSALES_CARTA_RENOVACION', '2,3,67,51');