package com.nulp.dss.excelmanaging.container;

public class DEKWinterResultFullStatisticContainer {

	private DEKResultFullStatisticContainer masters = new DEKResultFullStatisticContainer();
	private DEKResultFullStatisticContainer specialists = new DEKResultFullStatisticContainer();
	private DEKResultFullStatisticContainer mastersAbsentia = new DEKResultFullStatisticContainer();
	private DEKResultFullStatisticContainer specialistsAbsentia = new DEKResultFullStatisticContainer();

	public DEKResultFullStatisticContainer getMasters() {
		return masters;
	}

	public void setMasters(DEKResultFullStatisticContainer masters) {
		this.masters = masters;
	}

	public DEKResultFullStatisticContainer getSpecialists() {
		return specialists;
	}

	public void setSpecialists(DEKResultFullStatisticContainer specialists) {
		this.specialists = specialists;
	}

	public DEKResultFullStatisticContainer getMastersAbsentia() {
		return mastersAbsentia;
	}

	public void setMastersAbsentia(
			DEKResultFullStatisticContainer mastersAbsentia) {
		this.mastersAbsentia = mastersAbsentia;
	}

	public DEKResultFullStatisticContainer getSpecialistsAbsentia() {
		return specialistsAbsentia;
	}

	public void setSpecialistsAbsentia(
			DEKResultFullStatisticContainer specialistsAbsentia) {
		this.specialistsAbsentia = specialistsAbsentia;
	}

}
