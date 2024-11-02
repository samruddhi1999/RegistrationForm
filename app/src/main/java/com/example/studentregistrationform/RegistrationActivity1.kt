package com.example.studentregistrationform

import android.app.Activity
import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import com.shuhart.stepview.StepView
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.provider.MediaStore
import android.util.Log
import android.view.inputmethod.InputBinding
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.example.studentregistrationform.databinding.ActivityRegistration1Binding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RegistrationActivity1 : AppCompatActivity() {

    private lateinit var binding: ActivityRegistration1Binding
    private lateinit var databaseHelper: DatabaseHelper


    //Buttons
    private lateinit var bBack: Button
    private lateinit var bNext: Button
    private lateinit var bSendOtp: Button
    private lateinit var bsubmit: Button

    //TextFields
    //Personal Details
    private lateinit var etName: EditText
    private lateinit var etMiddleName: EditText
    private lateinit var etLastName: EditText

    private lateinit var sGenderSelect: Spinner
    private lateinit var etAadhar: EditText

    //Education Info
    private lateinit var etSchool: EditText
    private lateinit var etCourse: Spinner
    private lateinit var etRollNo: EditText
    private lateinit var etTenure: Spinner
    private lateinit var etAdmitYear: Spinner
    private lateinit var etStartDate: EditText
    private lateinit var etEndDate: EditText
    private lateinit var etDepot: Spinner
    private lateinit var etStartPnt: Spinner
    private lateinit var etEndPnt: Spinner
    private lateinit var etRoute: Spinner

    //CurrentAddress
    private lateinit var etBuilding: EditText
    private lateinit var etUnitNo: EditText
    private lateinit var etStreet: EditText
    private lateinit var etCity: EditText
    private lateinit var etDist: EditText
    private lateinit var etState: EditText
    private lateinit var etPin: EditText

    //CreatAcc
    private lateinit var etPhone: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etOtp: EditText

    private lateinit var stepView: StepView

    //LinearLayouts
    private lateinit var personalDetails: LinearLayout
    private lateinit var educationDetails: LinearLayout
    private lateinit var addressDetails: LinearLayout
    private lateinit var createAcc: LinearLayout
    private lateinit var insertData: LinearLayout

    //ImageView
    private val PICK_IMAGE_REQUEST = 1
    private val CAPTURE_IMAGE_REQUEST = 2
    private lateinit var imageView: ImageView

    //DatePicker
    private lateinit var dateOfBirth:EditText

    private var step: Int = 0
    private var currentStep: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistration1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseHelper = DatabaseHelper(this)

        // Initialize views
        bBack = findViewById(R.id.btnBack)
        bNext = findViewById(R.id.btnnext)
        bsubmit = findViewById(R.id.submit)


        etName = findViewById(R.id.enterName)
        etMiddleName = findViewById(R.id.middleName)
        etLastName = findViewById(R.id.lastName)
        sGenderSelect = findViewById(R.id.spinnerList)
        etAadhar = findViewById(R.id.aadharNum)
        //ImageView

        imageView = findViewById(R.id.viewImage)

        //Educational Info
        etSchool = findViewById(R.id.schoolName)
        etCourse = findViewById(R.id.courseName)
        etRollNo = findViewById(R.id.rollNo)
        etTenure = findViewById(R.id.courseTenure)
        etAdmitYear = findViewById(R.id.admitDate)

        etDepot = findViewById(R.id.depotName)
        etStartPnt = findViewById(R.id.startPoint)
        etEndPnt = findViewById(R.id.endPoint)
        etRoute = findViewById(R.id.viaRoute)

        //CurrentAddress
        etUnitNo = findViewById(R.id.unitNo)
        etBuilding = findViewById(R.id.buldngName)
        etStreet = findViewById(R.id.street)
        etCity = findViewById(R.id.cityN)
        etDist = findViewById(R.id.distr)
        etState = findViewById(R.id.state)
        etPin = findViewById(R.id.pinCode)

        //CreateAccnt
        etPhone = findViewById(R.id.tPhoneNum)
        etEmail = findViewById(R.id.eMail)
        etPassword = findViewById(R.id.passwords)
        etOtp = findViewById(R.id.otpNum)

        stepView = findViewById(R.id.step_view)

        //StepView Steps
        personalDetails = findViewById(R.id.personaldetails)
        educationDetails = findViewById(R.id.educationInfo)
        addressDetails = findViewById(R.id.currentAddress)
        createAcc = findViewById(R.id.createAccJCTSL)
        insertData = findViewById(R.id.displayData)


        //DatePickers
        dateOfBirth = findViewById(R.id.birthDate)
        etStartDate = findViewById(R.id.startDate)
        etEndDate = findViewById(R.id.endDate)



        //Spinners Function Call
        setupSpinner(etCourse, R.array.course_array)
        setupSpinner(etTenure, R.array.tenure_array)
        setupSpinner(etAdmitYear, R.array.admitYrs_array)
        setupSpinner(etDepot, R.array.depot_array)
        setupSpinner(etStartPnt, R.array.startPnt_array)
        setupSpinner(etEndPnt, R.array.endPnt_array)
        setupSpinner(etRoute, R.array.viaRoute_array)


        dateOfBirth.setOnClickListener{
            val currentDate = Calendar.getInstance()
            val maxDate = Calendar.getInstance()
            maxDate.add(Calendar.YEAR, -6) // Maximum age is 6 years
            val minDate = Calendar.getInstance()
            minDate.add(Calendar.YEAR, -27) // Minimum age is 27 years

            val datePickerDialog = DatePickerDialog(
                this,
                { _: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(year, month, dayOfMonth)

                    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val formattedDate: String = sdf.format(selectedDate.time)

                    dateOfBirth.setText(formattedDate)
                },
                currentDate.get(Calendar.YEAR),
                currentDate.get(Calendar.MONTH),
                currentDate.get(Calendar.DAY_OF_MONTH)
            )

            datePickerDialog.datePicker.maxDate = maxDate.timeInMillis
            datePickerDialog.datePicker.minDate = minDate.timeInMillis

            datePickerDialog.show()
        }

        imageView.setOnClickListener {
            showImageChooser()
        }

        fun hideKeyboard(activity: Activity) {
            val inputMethodManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            val currentFocusView = activity.currentFocus
            if (currentFocusView != null) {
                inputMethodManager.hideSoftInputFromWindow(currentFocusView.windowToken, 0)
            }
        }

        //spinner
        val spinner = findViewById<Spinner>(R.id.spinnerList)
        val genderList = arrayOf("Select Gender","Male", "Female", "Others")
        val arrayAdapter =  ArrayAdapter(this, android.R.layout.simple_spinner_item, genderList)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = arrayAdapter

        spinner.onItemSelectedListener = object :


            AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position == 0) {
                    // This is the hint, do something or show/hide elements accordingly
                } else {
                    val selectedGender = genderList[position]
                    // Handle the selected gender
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        etCourse.onItemSelectedListener= object: AdapterView.OnItemSelectedListener{

            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                val selectedCourse = parentView?.getItemAtPosition(position) as String
            }
            override fun onNothingSelected(AdapterView: AdapterView<*>?) {
            }
        }

        etCourse.onItemSelectedListener= object: AdapterView.OnItemSelectedListener{

            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                val selectedCourse = parentView?.getItemAtPosition(position) as String
            }
            override fun onNothingSelected(AdapterView: AdapterView<*>?) {
            }
        }
        etTenure.onItemSelectedListener= object: AdapterView.OnItemSelectedListener{

            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                val selectedCourse = parentView?.getItemAtPosition(position) as String
            }
            override fun onNothingSelected(AdapterView: AdapterView<*>?) {
            }
        }
        etAdmitYear.onItemSelectedListener= object: AdapterView.OnItemSelectedListener{

            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                val selectedCourse = parentView?.getItemAtPosition(position) as String
            }
            override fun onNothingSelected(AdapterView: AdapterView<*>?) {
            }
        }
        etDepot.onItemSelectedListener= object: AdapterView.OnItemSelectedListener{

            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                val selectedCourse = parentView?.getItemAtPosition(position) as String
            }
            override fun onNothingSelected(AdapterView: AdapterView<*>?) {
            }
        }
        etStartPnt.onItemSelectedListener= object: AdapterView.OnItemSelectedListener{

            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                val selectedCourse = parentView?.getItemAtPosition(position) as String
            }
            override fun onNothingSelected(AdapterView: AdapterView<*>?) {
            }
        }
        etEndPnt.onItemSelectedListener= object: AdapterView.OnItemSelectedListener{

            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                val selectedCourse = parentView?.getItemAtPosition(position) as String
            }
            override fun onNothingSelected(AdapterView: AdapterView<*>?) {
            }
        }
        //Next Button working
        bNext.setOnClickListener {
            //Personal Details
            if (step == 0) {

                if (etName.text.isEmpty()) {
                    etName.error = "This field is Required"
                    return@setOnClickListener
                }
                if (etMiddleName.text.isEmpty()) {
                    etMiddleName.error = "This field is Required"
                    return@setOnClickListener
                }
                if (etLastName.text.isEmpty()) {
                    etLastName.error = "This field is Required"
                    return@setOnClickListener
                }
                hideKeyboard(this)

                val selectedDate = dateOfBirth.text.toString()
                if (selectedDate.isEmpty()) {
                    dateOfBirth.error = "This field is Required"
                    return@setOnClickListener
                }

                val selectedGender = genderList[spinner.selectedItemPosition]
                if (selectedGender == "Select Gender") {
                    // Show an error message for the Spinner
                    (spinner.getChildAt(0) as? TextView)?.error = "This field is Required"
                    return@setOnClickListener
                }

                val aadharNo = etAadhar.text.toString()
                if (etAadhar.text.isEmpty()) {
                    etAadhar.error = "This field is Required"
                    return@setOnClickListener
                }
                if (aadharNo.length != 12){
                    etAadhar.error = "Entered 12 digit valid aadhar No."
                    return@setOnClickListener
                }

                //Educational Details
            } else if (step == 1) {
                if (etSchool.text.isEmpty()) {
                    etSchool.error = "This field is Required"
                    return@setOnClickListener
                }

                val selectedCrsArray = resources.getStringArray(R.array.course_array)
                val selectedCourse = selectedCrsArray[etCourse.selectedItemPosition]
                if (selectedCourse  == "Select Course"){
                    (etCourse.getChildAt(0) as TextView)?.error = "This Field is Required"
                    return@setOnClickListener
                }

                if(etRollNo.text.isEmpty()){
                    etRollNo.error = "This Field is Required"
                    return@setOnClickListener
                }

                val selectedTnrArray = resources.getStringArray(R.array.tenure_array)
                val selectedTenure = selectedTnrArray[etTenure.selectedItemPosition]
                if (selectedTenure == "Select Course Tenure"){
                    (etTenure.getChildAt(0) as TextView)?.error = "This field is Required"
                    return@setOnClickListener
                }

                val selectedAdmnYArray = resources.getStringArray(R.array.admitYrs_array)
                val selectedAdmnY = selectedAdmnYArray[etAdmitYear.selectedItemPosition]
                if (selectedAdmnY == "Select Admission Year"){
                    (etAdmitYear.getChildAt(0) as TextView)?.error = "This Field is Required"
                    return@setOnClickListener
                }

                val selectedStDate = etStartDate.text.toString()
                if (selectedStDate.isEmpty()) {
                    etStartDate.error = "This field is Required"
                    return@setOnClickListener
                }

                val selectedEndDate = etEndDate.text.toString()
                if (selectedEndDate.isEmpty()) {
                    etEndDate.error = "This field is Required"
                    return@setOnClickListener
                }


                val selectedDepotArray = resources.getStringArray(R.array.depot_array)
                val selectedDepot = selectedDepotArray[etDepot.selectedItemPosition]
                if (selectedDepot == "Select Depot"){
                    (etDepot.getChildAt(0) as TextView)?.error = "This Field is Required"
                    return@setOnClickListener
                }

                val selectedSPntArray = resources.getStringArray(R.array.startPnt_array)
                val selectedStPnt = selectedSPntArray[etStartPnt.selectedItemPosition]
                if (selectedStPnt == "Select Start Point"){
                    (etStartPnt.getChildAt(0) as TextView)?.error = "This Field is Required"
                    return@setOnClickListener
                }

                val selectedEPntArray = resources.getStringArray(R.array.endPnt_array)
                val selectedEdPnt = selectedEPntArray[etEndPnt.selectedItemPosition]
                if (selectedEdPnt == "Select End point"){
                    (etEndPnt.getChildAt(0) as TextView)?.error = "This Field is Required"
                    return@setOnClickListener
                }

                val selectedRoutArray = resources.getStringArray(R.array.viaRoute_array)
                val selectedRoute = selectedRoutArray[etRoute.selectedItemPosition]
                if (selectedRoute == "Select Via Route"){
                    (etRoute.getChildAt(0) as TextView)?.error = "This Field is Required"
                    return@setOnClickListener
                }

            } else if (step == 2) {
                if (etUnitNo.text.isEmpty()) {
                    etUnitNo.error = "This field is Required"
                    return@setOnClickListener
                }
                if (etBuilding.text.isEmpty()) {
                    etBuilding.error = "This field is Required"
                    return@setOnClickListener
                }
                if (etStreet.text.isEmpty()) {
                    etStreet.error = "This field is Required"
                    return@setOnClickListener
                }
                if (etCity.text.isEmpty()) {
                    etCity.error = "This field is Required"
                    return@setOnClickListener
                }
                if (etDist.text.isEmpty()) {
                    etDist.error = "This field is Required"
                    return@setOnClickListener
                }

                if (etState.text.isEmpty()) {
                    etState.error = "This field is Required"
                    return@setOnClickListener
                }

                val pinNum = etPin.text.toString()
                if (etPin.text.isEmpty()) {
                    etPin.error = "This field is Required"
                    return@setOnClickListener
                }

                if (pinNum.length != 6){
                    etPin.error = "Enter valid pin No."
                    return@setOnClickListener
                }

            } else if (step == 3) {

                val phoneNumL = etPhone.text.toString()
                if (etPhone.text.isEmpty()){
                    etPhone.error = "This Field is Required"
                    return@setOnClickListener
                }
                if (phoneNumL.length != 10){
                    etPhone.error = "Enter Valid Number"
                    return@setOnClickListener
                }

                val emailToText = etEmail.text.toString()
                if (emailToText.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(emailToText).matches()) {
                    etEmail.error = "Enter a valid email address"
                    return@setOnClickListener
                }
                val passW = etPassword.text.toString()
                if (etPassword.text.isEmpty()){
                    etPassword.error = "This field is Required"
                    return@setOnClickListener
                }
            }
            when (step) {
                0 -> {
                    personalDetails.visibility = View.GONE
                    educationDetails.visibility = View.VISIBLE
                    step = 1
                    stepView.done(false)
                    stepView.go(step, true)
                    bBack.visibility = View.VISIBLE
                    bsubmit.visibility = View.GONE
                }
                1 -> {
                    educationDetails.visibility = View.GONE
                    addressDetails.visibility = View.VISIBLE
                    step = 2
                    stepView.done(false)
                    stepView.go(step, true)
                    bsubmit.visibility = View.GONE
                }
                2 -> {
                    addressDetails.visibility = View.GONE
                    createAcc.visibility = View.VISIBLE
                    step = 3
                    stepView.done(false)
                    stepView.go(step, true)
                    bsubmit.visibility = View.GONE
                }
                3 ->{
                    createAcc.visibility = View.GONE
                    insertData.visibility = View.VISIBLE
                    bsubmit.visibility = View.VISIBLE
                }
            }
        }
        bBack.setOnClickListener {

            if(currentStep > 0){
                currentStep--
            }
            stepView.done(false)
            stepView.go(currentStep,true)

            when(step){

                0 ->{  /*bsubmit.visibility = View.GONE*/ }
                1 ->{
                    educationDetails.visibility  = View.GONE
                    personalDetails.visibility = View.VISIBLE
                    step = 0
                    stepView.done(false)
                    stepView.go(step, true)
                    bBack.visibility = View.INVISIBLE
                    hideKeyboard(this)
                    bNext.visibility = View.VISIBLE
                   // bsubmit.visibility = View.GONE
                }

                2 ->{
                    addressDetails.visibility = View.GONE
                    educationDetails.visibility = View.VISIBLE
                    step = 1
                    stepView.done(false)
                    stepView.go(step, true)
                    bNext.visibility = View.VISIBLE

                }

                3 ->{
                    createAcc.visibility = View.GONE
                    addressDetails.visibility = View.VISIBLE
                    step = 2
                    stepView.done(false)
                    stepView.go(step, true)
                    bNext.visibility = View.VISIBLE

                }
                4 ->{
                    createAcc.visibility = View.VISIBLE
                    insertData.visibility = View.GONE
                    bsubmit.visibility = View.GONE

                    step = 3
                    stepView.done(false)
                    stepView.go(step, true)
                    bNext.visibility = View.VISIBLE

                }
            }
        }
        bsubmit.setOnClickListener {

            val enterName = etName.text.toString()
            val middleName = etMiddleName.text.toString()
            val lastName = etLastName.text.toString()
            val birthDate = dateOfBirth.text.toString()
            val spinnerList = spinner.selectedItem.toString()
            val aadharNum = etAadhar.text.toString()

            studentsDatabase(enterName, middleName, lastName, birthDate, spinnerList, aadharNum)
        }
        etStartDate.setOnClickListener{
            showDatePickerDialog(etStartDate)
        }
        etEndDate.setOnClickListener{
            showDatePickerDialog(etEndDate)
        }
    }
    private fun setupSpinner(spinner: Spinner?, arrayResID: Int) {

        ArrayAdapter.createFromResource(
            this,
            arrayResID,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
            spinner?.adapter = adapter
        }
    }

    private fun showImageChooser(){

        val options = arrayOf("Take Photo","Chose from Gallery", "Cancel")
        val builder = AlertDialog.Builder(this)

        builder.setTitle("Choose an Option")
        builder.setItems(options){ dialog, which ->
            when(which){
                0 -> takePhotoFromCamera()
                1 -> choosePhotoFromGallary()
                2 -> dialog.dismiss()
            }
        }
        builder.show()
    }


    private fun takePhotoFromCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, CAPTURE_IMAGE_REQUEST)
    }
    private fun choosePhotoFromGallary() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK){
            when(requestCode){
                PICK_IMAGE_REQUEST -> {
                    val selectedImageUri = data?.data
                    imageView.setImageURI(selectedImageUri)
                }
                CAPTURE_IMAGE_REQUEST -> {
                    val photo = data?.extras?.get("data") as Bitmap
                    imageView.setImageBitmap(photo)
                }
            }

        }
    }
    private fun showDatePickerDialog(editText: EditText){
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = "$selectedYear-${selectedMonth + 1}-$selectedDay"
                editText.setText(selectedDate)
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }
    private fun studentsDatabase(
        FirstName: String,
        MiddleName: String,
        LastName: String,
        DOB: String,
        Gender: String,
        AadharNo: String,
    ){
        val insertRowId = databaseHelper.insertStudentData(FirstName, MiddleName, LastName, DOB, Gender, AadharNo)
        Log.d("Register", "$insertRowId")

        if (insertRowId != 1L){
            Toast.makeText(this, "Registered Successfully", Toast.LENGTH_SHORT).show()

            val intent = Intent(this,DisplayStudentData::class.java)
            startActivity(intent)
            finish()

        }
        else{
            Toast.makeText(this, "Try Again", Toast.LENGTH_SHORT).show()
        }

    }
}
