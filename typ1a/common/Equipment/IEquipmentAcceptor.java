package com.typ1a.common.Equipment;

import com.typ1a.common.Kinematics;

/**Implementors must remember to onUpdate their facade*/
public interface IEquipmentAcceptor {
	public EquipmentFacade getEquipmentFacade();
	public Kinematics getKinematics();
}
