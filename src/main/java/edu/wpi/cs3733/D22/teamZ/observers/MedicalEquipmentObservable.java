package edu.wpi.cs3733.D22.teamZ.observers;

public interface MedicalEquipmentObservable {
  public void registerObserver(MedicalEquipmentObserver observer);

  public void notifyObservers();

  public void removeObserver(MedicalEquipmentObserver observer);
}
