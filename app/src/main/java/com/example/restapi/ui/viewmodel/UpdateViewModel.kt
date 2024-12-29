package com.example.restapi.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restapi.model.Mahasiswa
import com.example.restapi.repository.MahasiswaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


data class UpdateUiEvent(
    val nim: String,
    val nama: String,
    val alamat: String,
    val jenisKelamin: String,
    val kelas: String,
    val angkatan: String
)


sealed class UpdateUiState {
    object Idle : UpdateUiState()
    object Loading : UpdateUiState()
    data class Success(val mahasiswa: Mahasiswa) : UpdateUiState()
    data class Error(val message: String) : UpdateUiState()
}

class UpdateViewModel(
    private val mahasiswaRepository: MahasiswaRepository
) : ViewModel() {


    private val _uiState = MutableStateFlow<UpdateUiState>(UpdateUiState.Idle)
    val uiState: StateFlow<UpdateUiState> = _uiState


    private var currentFormData: UpdateUiEvent? = null


    fun loadMahasiswaData(nim: String) {
        _uiState.value = UpdateUiState.Loading
        viewModelScope.launch {
            try {
                val mahasiswa = mahasiswaRepository.getMahasiswabyNim(nim)
                currentFormData = UpdateUiEvent(
                    nim = mahasiswa.nim,
                    nama = mahasiswa.nama,
                    alamat = mahasiswa.alamat,
                    jenisKelamin = mahasiswa.jenisKelamin,
                    kelas = mahasiswa.kelas,
                    angkatan = mahasiswa.angkatan
                )
                _uiState.value = UpdateUiState.Success(mahasiswa)
            } catch (e: Exception) {
                _uiState.value = UpdateUiState.Error("Failed to load mahasiswa data: ${e.message}")
            }
        }
    }


    fun updateUiState(event: UpdateUiEvent) {
        currentFormData = event
    }

    fun updateMahasiswa() {
        val formData = currentFormData
        if (formData == null) {
            _uiState.value = UpdateUiState.Error("Form data is not initialized")
            return
        }

        _uiState.value = UpdateUiState.Loading
        viewModelScope.launch {
            try {
                val mahasiswa = Mahasiswa(
                    nim = formData.nim,
                    nama = formData.nama,
                    alamat = formData.alamat,
                    jenisKelamin = formData.jenisKelamin,
                    kelas = formData.kelas,
                    angkatan = formData.angkatan
                )
                mahasiswaRepository.updateMahasiswa(formData.nim, mahasiswa)
                _uiState.value = UpdateUiState.Success(mahasiswa)
            } catch (e: Exception) {
                _uiState.value = UpdateUiState.Error("Failed to update mahasiswa: ${e.message}")
            }
        }
    }
}