
UPDATE d_ciclos_grupales as dcg
JOIN 
	( 
		select t1.*,if(t1.total_integrantes > t1.total_asegurados,false,true) as conSeguroCompleto
		from
		(
			select 
				ic_numgrupo,
				ic_numciclo,
				count(*) as total_integrantes,
				sum(CASE WHEN se_contratacion = 1 THEN 1 ELSE 0 END) as total_asegurados 
			from d_integrantes_ciclo intcic
			inner join d_seguros seg
			on intcic.ic_numcliente=seg.se_numcliente 
			and intcic.ic_numsolicitud = seg.se_numsolicitud
			group by ic_numgrupo,ic_numciclo
		) as t1
	)  as t2
ON dcg.ci_numgrupo=t2.ic_numgrupo and dcg.ci_numciclo=t2.ic_numciclo

SET dcg.ci_consegurocompleto = t2.conSeguroCompleto;