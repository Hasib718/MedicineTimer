package com.example.medicinetimer.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.medicinetimer.container.Medicine;

import java.util.List;

@Dao
public interface MedicineDao {

    @Insert
    void insertMedicine(Medicine medicine);

    @Update
    void updateMedicine(Medicine medicine);

    @Delete
    void deleteMedicine(Medicine medicine);

    @Query("SELECT * FROM MEDICINE ORDER BY ID")
    List<Medicine> loadAllMedicines();

    @Query("SELECT * FROM MEDICINE WHERE ID = :id")
    Medicine loadMedicineById(int id);

    @Query("SELECT * FROM MEDICINE WHERE startingDay= :startingDay")
    Medicine loadMedicineByStartingDay(String startingDay);
}
