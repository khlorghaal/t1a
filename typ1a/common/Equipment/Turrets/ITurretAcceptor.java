package com.typ1a.common.Equipment.Turrets;

import com.typ1a.common.Equipment.IEquipmentAcceptor;

public interface ITurretAcceptor extends IEquipmentAcceptor{
	public TurretMount getTurretMount(int indx);
}
