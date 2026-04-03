package com.example.empty_views

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class Tugas5Activity : AppCompatActivity() {

    private lateinit var tilNama: TextInputLayout
    private lateinit var tilEmail: TextInputLayout
    private lateinit var tilPassword: TextInputLayout
    private lateinit var tilConfirmPassword: TextInputLayout

    private lateinit var etNama: TextInputEditText
    private lateinit var etEmail: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var etConfirmPassword: TextInputEditText

    private lateinit var tvPasswordError: TextView
    private lateinit var tvHobiCount: TextView

    private lateinit var rgJenisKelamin: RadioGroup
    private lateinit var spinnerKota: Spinner

    private lateinit var cbOlahraga: CheckBox
    private lateinit var cbMusik: CheckBox
    private lateinit var cbCoding: CheckBox
    private lateinit var cbMemasak: CheckBox
    private lateinit var cbMembaca: CheckBox
    private lateinit var cbGaming: CheckBox

    private lateinit var btnDaftar: Button
    private lateinit var btnReset: Button

    private val kotaList = listOf(
        "Pilih kota...", "Bandung", "Jakarta", "Surabaya",
        "Yogyakarta", "Medan", "Makassar", "Semarang", "Palembang"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tugas5)

        bindViews()
        setupSpinner()
        setupTextWatchers()
        setupCheckboxListeners()
        setupButtons()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun bindViews() {
        tilNama            = findViewById(R.id.tilNama)
        tilEmail           = findViewById(R.id.tilEmail)
        tilPassword        = findViewById(R.id.tilPassword)
        tilConfirmPassword = findViewById(R.id.tilConfirmPassword)

        etNama            = findViewById(R.id.etNama)
        etEmail           = findViewById(R.id.etEmail)
        etPassword        = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)

        tvPasswordError = findViewById(R.id.tvPasswordError)
        tvHobiCount     = findViewById(R.id.tvHobiCount)

        rgJenisKelamin = findViewById(R.id.rgJenisKelamin)
        spinnerKota    = findViewById(R.id.spinnerKota)

        cbOlahraga = findViewById(R.id.cbOlahraga)
        cbMusik    = findViewById(R.id.cbMusik)
        cbCoding   = findViewById(R.id.cbCoding)
        cbMemasak  = findViewById(R.id.cbMemasak)
        cbMembaca  = findViewById(R.id.cbMembaca)
        cbGaming   = findViewById(R.id.cbGaming)

        btnDaftar = findViewById(R.id.btnDaftar)
        btnReset  = findViewById(R.id.btnReset)
    }

    private fun setupSpinner() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, kotaList).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        spinnerKota.adapter = adapter
    }

    private fun setupTextWatchers() {
        etNama.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().trim().isEmpty()) tilNama.error = "Nama tidak boleh kosong"
                else { tilNama.error = null; tilNama.isErrorEnabled = false }
            }
            override fun beforeTextChanged(s: CharSequence?, st: Int, c: Int, a: Int) {}
            override fun onTextChanged(s: CharSequence?, st: Int, b: Int, c: Int) {}
        })

        etEmail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val email = s.toString().trim()
                when {
                    email.isEmpty() -> tilEmail.error = "Email tidak boleh kosong"
                    !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
                        tilEmail.error = "Format email tidak valid"
                    else -> { tilEmail.error = null; tilEmail.isErrorEnabled = false }
                }
            }
            override fun beforeTextChanged(s: CharSequence?, st: Int, c: Int, a: Int) {}
            override fun onTextChanged(s: CharSequence?, st: Int, b: Int, c: Int) {}
        })

        etPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val pass = s.toString()
                when {
                    pass.isEmpty() -> tilPassword.error = "Password tidak boleh kosong"
                    pass.length < 8 -> tilPassword.error = "Password minimal 8 karakter"
                    else -> { tilPassword.error = null; tilPassword.isErrorEnabled = false }
                }
                validatePasswordMatch()
            }
            override fun beforeTextChanged(s: CharSequence?, st: Int, c: Int, a: Int) {}
            override fun onTextChanged(s: CharSequence?, st: Int, b: Int, c: Int) {}
        })

        etConfirmPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) { validatePasswordMatch() }
            override fun beforeTextChanged(s: CharSequence?, st: Int, c: Int, a: Int) {}
            override fun onTextChanged(s: CharSequence?, st: Int, b: Int, c: Int) {}
        })
    }

    private fun validatePasswordMatch() {
        val pass    = etPassword.text.toString()
        val confirm = etConfirmPassword.text.toString()
        when {
            confirm.isEmpty() -> tvPasswordError.visibility = View.GONE
            pass != confirm -> {
                tvPasswordError.text = "Password tidak cocok"
                tvPasswordError.visibility = View.VISIBLE
            }
            else -> tvPasswordError.visibility = View.GONE
        }
    }

    private fun setupCheckboxListeners() {
        listOf(cbOlahraga, cbMusik, cbCoding, cbMemasak, cbMembaca, cbGaming)
            .forEach { it.setOnCheckedChangeListener { _, _ -> updateHobiCount() } }
    }

    private fun updateHobiCount(): Int {
        val count = listOf(cbOlahraga, cbMusik, cbCoding, cbMemasak, cbMembaca, cbGaming)
            .count { it.isChecked }
        when {
            count < 3 -> {
                tvHobiCount.text = if (count == 0)
                    "0 hobi dipilih — pilih minimal 3"
                else
                    "$count hobi dipilih — pilih minimal 3"
                tvHobiCount.setTextColor(Color.parseColor("#ff8a8a"))
            }
            else -> {
                tvHobiCount.text = "$count hobi dipilih — syarat terpenuhi"
                tvHobiCount.setTextColor(Color.parseColor("#7bd0ff"))
            }
        }
        return count
    }

    private fun setupButtons() {
        btnDaftar.setOnClickListener {
            if (validateAll()) showConfirmDialog()
        }
        btnDaftar.setOnLongClickListener {
            showResetConfirmDialog()
            true
        }
        btnReset.setOnClickListener { resetForm() }
    }

    private fun validateAll(): Boolean {
        var isValid = true
        val nama    = etNama.text.toString().trim()
        val email   = etEmail.text.toString().trim()
        val pass    = etPassword.text.toString()
        val confirm = etConfirmPassword.text.toString()

        if (nama.isEmpty()) { tilNama.error = "Nama tidak boleh kosong"; isValid = false }
        if (email.isEmpty()) { tilEmail.error = "Email tidak boleh kosong"; isValid = false }
        else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.error = "Format email tidak valid"; isValid = false
        }
        if (pass.isEmpty()) { tilPassword.error = "Password tidak boleh kosong"; isValid = false }
        else if (pass.length < 8) { tilPassword.error = "Password minimal 8 karakter"; isValid = false }
        if (confirm.isEmpty() || pass != confirm) {
            tvPasswordError.text = if (confirm.isEmpty())
                "Konfirmasi password wajib diisi" else "Password tidak cocok"
            tvPasswordError.visibility = View.VISIBLE
            isValid = false
        }
        if (rgJenisKelamin.checkedRadioButtonId == -1) {
            Toast.makeText(this, "Pilih jenis kelamin", Toast.LENGTH_SHORT).show()
            isValid = false
        }
        if (updateHobiCount() < 3) {
            Toast.makeText(this, "Pilih minimal 3 hobi", Toast.LENGTH_SHORT).show()
            isValid = false
        }
        if (spinnerKota.selectedItemPosition == 0) {
            Toast.makeText(this, "Pilih kota asal", Toast.LENGTH_SHORT).show()
            isValid = false
        }
        return isValid
    }

    private fun showConfirmDialog() {
        val nama   = etNama.text.toString().trim()
        val email  = etEmail.text.toString().trim()
        val kota   = spinnerKota.selectedItem.toString()
        val gender = if (rgJenisKelamin.checkedRadioButtonId == R.id.rbLakiLaki)
            "Laki-laki" else "Perempuan"
        val hobi = listOf(cbOlahraga, cbMusik, cbCoding, cbMemasak, cbMembaca, cbGaming)
            .filter { it.isChecked }
            .joinToString(", ") { it.text.toString() }

        AlertDialog.Builder(this)
            .setTitle("Konfirmasi Registrasi")
            .setMessage("Nama    : $nama\nEmail   : $email\nGender  : $gender\nKota    : $kota\nHobi    : $hobi\n\nKonfirmasi data di atas?")
            .setPositiveButton("Daftar") { dialog, _ ->
                dialog.dismiss()
                Toast.makeText(this, "Registrasi berhasil!", Toast.LENGTH_LONG).show()
                resetForm()
            }
            .setNegativeButton("Batal") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun showResetConfirmDialog() {
        AlertDialog.Builder(this)
            .setTitle("Reset Form")
            .setMessage("Yakin ingin mengosongkan semua isian?")
            .setPositiveButton("Ya, reset") { dialog, _ ->
                dialog.dismiss()
                resetForm()
                Toast.makeText(this, "Form direset", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Batal") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun resetForm() {
        etNama.setText(""); etEmail.setText("")
        etPassword.setText(""); etConfirmPassword.setText("")

        tilNama.error = null;            tilNama.isErrorEnabled = false
        tilEmail.error = null;           tilEmail.isErrorEnabled = false
        tilPassword.error = null;        tilPassword.isErrorEnabled = false
        tilConfirmPassword.error = null; tilConfirmPassword.isErrorEnabled = false
        tvPasswordError.visibility = View.GONE

        rgJenisKelamin.check(R.id.rbLakiLaki)
        listOf(cbOlahraga, cbMusik, cbCoding, cbMemasak, cbMembaca, cbGaming)
            .forEach { it.isChecked = false }
        updateHobiCount()
        spinnerKota.setSelection(0)
    }
}