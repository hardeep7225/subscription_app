package com.example.subscriptionapplication

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.subscriptionapplication.adapter.ServiceAdapter
import com.example.subscriptionapplication.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity(), CallbackServiceSelected {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ServiceAdapter
    private val calendar = Calendar.getInstance()

    private var frequencySelected = ""
    private val startedDateSelected = ""
    private var categorySelected = ""
    private var activeService = false


    private val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH)

    private val services = mutableListOf(
        Service(1, "Netflix", "netflix", false, 199),
        Service(2, "Hulu", "hulu", false, 149),
        Service(3, "Spotify", "spoticon", false, 99),
        Service(4, "Playstation +", "psicon", false, 299),
        Service(5, "Paramount +", "paramount", false, 179),
        Service(6, "You Tube Music", "utube", false, 129)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Handle system bars
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.tvServiceStartDate.text = dateFormat.format(calendar.time)

        // Initialize RecyclerView
        adapter = ServiceAdapter(this, services, this)
        binding.rvServices.layoutManager = LinearLayoutManager(this)
        binding.rvServices.adapter = adapter

        setupClickListeners()
        setupSearchListener()
    }

    private fun setupClickListeners() {
        binding.ivChooseService.setOnClickListener {
            binding.ctServiceDropDown.visibility = View.VISIBLE
        }

        binding.ctChooseServiceWithUpDown.setOnClickListener {
            binding.ctServiceDropDown.visibility = View.VISIBLE
        }

        binding.swServiceActive.setOnCheckedChangeListener { _, isChecked ->
            activeService = isChecked
            //Log.d("ServiceStatus", "Service Active: $serviceActive")
        }

        binding.ctDeleteService.setOnClickListener {
            binding.tvServiceNameChosen.text = "Choose a service"
            binding.tvServiceCategoryChosen.text = "Subscription"
            binding.tvServiceFrequencyChosen.text = "Weekly"
            binding.tvServiceFrequencyChosen.text = "Choose a frequency"
            binding.tvChooseService.text = "Choose a service"
            binding.tvServiceNameChosen.text = "Choose a service"
            binding.tvServicePrice.text = "$ 0"
            binding.tvServiceAmountChosen.text = "$ 0"

            binding.swServiceActive.isActivated = false

            val resId = this.resources.getIdentifier("plus_round_icon", "drawable", this.packageName)
            binding.ivChooseService.setImageResource(resId)

            val ivCross = this.resources.getIdentifier("cross", "drawable", this.packageName)
            binding.ivTopNav.setImageResource(ivCross)

            val unselected = ContextCompat.getColor(this, R.color.unselected)
            binding.tvSave.setTextColor(unselected)
            binding.tvChooseService.setTextColor(unselected)
            binding.tvServicePrice.setTextColor(unselected)
            binding.tvServiceNameChosen.setTextColor(unselected)
            binding.tvServiceCategoryChosen.setTextColor(unselected)
            binding.tvServiceFrequencyChosen.setTextColor(unselected)

            binding.ctDeleteService.visibility = View.GONE

        }

        binding.tvDdServiceDone.setOnClickListener {
            binding.ctServiceDropDown.visibility = View.GONE
        }

        binding.tvBtmCategoryDone.setOnClickListener {
            if (!categorySelected.isEmpty()) {
                binding.tvServiceCategoryChosen.text = categorySelected
            }

            binding.ctCategoryView.visibility = View.GONE
        }

        binding.ctChooseSubscription.setOnClickListener {
            binding.ctCategoryView.visibility = View.VISIBLE
        }

        binding.ctBottomCategorySubscription.setOnClickListener {
            categorySelected = "Subscription"
            binding.ivBottomSubscriptionSelected.visibility = View.VISIBLE
            binding.ivBottomUtilitySelected.visibility = View.GONE
            binding.ivBottomCardSelected.visibility = View.GONE
            binding.ivBottomLoanSelected.visibility = View.GONE
            binding.ivBottomRentSelected.visibility = View.GONE

        }
        binding.ctBottomCategoryUtility.setOnClickListener {
            categorySelected = "Utility"
            binding.ivBottomSubscriptionSelected.visibility = View.GONE
            binding.ivBottomUtilitySelected.visibility = View.VISIBLE
            binding.ivBottomCardSelected.visibility = View.GONE
            binding.ivBottomLoanSelected.visibility = View.GONE
            binding.ivBottomRentSelected.visibility = View.GONE
             }
        binding.ctBottomCategoryCard.setOnClickListener {
            categorySelected = "Card"
            binding.ivBottomSubscriptionSelected.visibility = View.GONE
            binding.ivBottomUtilitySelected.visibility = View.GONE
            binding.ivBottomCardSelected.visibility = View.VISIBLE
            binding.ivBottomLoanSelected.visibility = View.GONE
            binding.ivBottomRentSelected.visibility = View.GONE
             }
        binding.ctBottomCategoryLoan.setOnClickListener {
            categorySelected = "Loan"
            binding.ivBottomSubscriptionSelected.visibility = View.GONE
            binding.ivBottomUtilitySelected.visibility = View.GONE
            binding.ivBottomCardSelected.visibility = View.GONE
            binding.ivBottomLoanSelected.visibility = View.VISIBLE
            binding.ivBottomRentSelected.visibility = View.GONE
             }
        binding.ctBottomCategoryRent.setOnClickListener {
            categorySelected = "Rent"
            binding.ivBottomSubscriptionSelected.visibility = View.GONE
            binding.ivBottomUtilitySelected.visibility = View.GONE
            binding.ivBottomCardSelected.visibility = View.GONE
            binding.ivBottomLoanSelected.visibility = View.GONE
            binding.ivBottomRentSelected.visibility = View.VISIBLE
             }

        //        Date Picker
        binding.tvBtmStartDateDone.setOnClickListener {
            binding.ctStartDateView.visibility = View.GONE
        }

        binding.ctStartDateClick.setOnClickListener {
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    // Update the calendar
                    calendar.set(selectedYear, selectedMonth, selectedDay)

                    // Format: Apr 14, 2025
                    val formattedDate = SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH)
                        .format(calendar.time)

                    // Show formatted date in TextView
                    binding.tvServiceStartDate.text = formattedDate
                },
                year,
                month,
                day
            )

            // ✅ Allow only dates from today up to 6 months ahead
            datePicker.datePicker.minDate = System.currentTimeMillis()
            datePicker.datePicker.maxDate =
                Calendar.getInstance().apply { add(Calendar.MONTH, 6) }.timeInMillis

            datePicker.show()
        }

        //        Frequency View Clicks

        binding.ctFrequencySelectClick.setOnClickListener {
            binding.ctBottomFrequencyView.visibility = View.VISIBLE
        }

        binding.tvBtmFrequencyDone.setOnClickListener {
            if (!frequencySelected.isEmpty()) {
                binding.tvServiceFrequencyChosen.text = frequencySelected
            }
            binding.tvServiceFrequencyChosen.text = frequencySelected
            binding.ctBottomFrequencyView.visibility = View.GONE
        }

        binding.ctBottomWeeklyFrequency.setOnClickListener {
            frequencySelected = "Weekly"
            binding.ivBottomWeeklySelected.visibility = View.VISIBLE
            binding.ivBottomMontlySelected.visibility = View.GONE
            binding.ivBottomYearlySelected.visibility = View.GONE
        }
        binding.ctBottomMontlyFrequency.setOnClickListener {
            frequencySelected = "Monthly"
            binding.ivBottomWeeklySelected.visibility = View.GONE
            binding.ivBottomMontlySelected.visibility = View.VISIBLE
            binding.ivBottomYearlySelected.visibility = View.GONE
        }
        binding.ctBottomYearlyFrequency.setOnClickListener {
            frequencySelected = "Yearly"
            binding.ivBottomWeeklySelected.visibility = View.GONE
            binding.ivBottomMontlySelected.visibility = View.GONE
            binding.ivBottomYearlySelected.visibility = View.VISIBLE
        }

    }

    private fun setupSearchListener() {
        // Assuming your EditText inside ctServiceDdSearch is named etSearch
        val etSearch = binding.ctServiceDdSearch.findViewById<android.widget.EditText>(R.id.etSearch)
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter.filter(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    override fun onServiceSelected(service: Service) {
        // Deselect all others
        services.forEach { it.selected = it == service }

        binding.ctDeleteService.visibility = View.VISIBLE

        // Update main UI
        binding.tvChooseService.text = service.name
        binding.tvServiceNameChosen.text = service.name

        binding.tvServicePrice.text = "$ ${service.price}"
        binding.tvServiceAmountChosen.text = "$ ${service.price}"

        // Change text color to black when selected
        val black = ContextCompat.getColor(this, android.R.color.black)
        binding.tvChooseService.setTextColor(black)
        binding.tvServicePrice.setTextColor(black)

        val blue = ContextCompat.getColor(this, R.color.blue)
        binding.tvSave.setTextColor(blue)

        val resId = this.resources.getIdentifier(service.image, "drawable", this.packageName)
        binding.ivChooseService.setImageResource(resId)

        val ivBackId = this.resources.getIdentifier("iv_back", "drawable", this.packageName)
        binding.ivTopNav.setImageResource(ivBackId)

        // Refresh adapter to reflect selection
        adapter.notifyDataSetChanged()

        // Optionally hide dropdown after selection
        binding.ctServiceDropDown.visibility = View.GONE
    }
}
