package com.artilearn.happygolfgo.deprecated

/**
 * Deprecated 2020.06.16 타석번호 제거
 */
//class SelectPlateActivity : BaseActivity() {
//    private val TAG = "SelectPlateActivity"
//
//    private lateinit var requestManager: RequestManager
//    private lateinit var userManager: HappyGolfGoUserManager
//    private lateinit var pt: PlateTime
//    private lateinit var ticket: Ticket
//    private lateinit var plateAdapter: PlateAdapter
//
//    private var day: String? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_select_plate)
//
//        requestManager = RequestManager(this)
//        userManager = HappyGolfGoUserManager(this)
//
//        setFrameLayoutID(R.id.frame_layout_select_plate)
//        setBaseFragments()
//
//        initData()
//        setUpReachability()
//        initDisplay()
//        initAdapter()
//    }
//
//    override fun retryAction() {
//        super.retryAction()
//        fetchData()
//    }
//
//    override fun onStart() {
//        super.onStart()
//        LocalBroadcastManager.getInstance(this)
//            .registerReceiver(mMessageReceiver, IntentFilter("network-status"))
//    }
//
//    override fun onPause() {
//        super.onPause()
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver)
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        if (this::requestManager.isInitialized) requestManager.rootActivity.finish()
//    }
//
//    private fun initData() {
//        pt = intent.getSerializableExtra("plate") as PlateTime
//        ticket = intent.getSerializableExtra("ticket") as Ticket
//        val clickDate = intent.getStringExtra("idx")
//
//        if (clickDate != null) day = clickDate
//
//        fetchData()
//    }
//
//    private fun initAdapter() {
//        plateAdapter = PlateAdapter(PlateAdapter.PlateClickListener { plate ->
//            when (plate.status.integer) {
//                0 -> {
//                    root.snackbar("예약이 되어 있습니다")
//                }
//                1 -> {
//                    val intent = Intent(this, ConfirmReservationActivity::class.java)
//                    intent.putExtra("item", plate)
//                    intent.putExtra("ticket", ticket)
//                    intent.putExtra("pt", pt)
//                    startActivity(intent)
//                }
//            }
//        })
//        recyclerView.adapter = plateAdapter
//    }
//
//    private fun initDisplay() {
//        setUpActionBar(
//            toolbar_select_plate.toolbar,
//            getString(R.string.toolbar_title_select_plate),
//            true
//        )
//    }
//
//    private fun isLoading(loading: Boolean) {
//        progressBar.visibility = if (loading) {
//            View.VISIBLE
//        } else {
//            View.INVISIBLE
//        }
//    }
//
//    private fun fetchData() {
//        isLoading(true)
//
//        val request = HappyGolfGoRequest.selectPlate
//        request.endpoint = pt.plateAvailabilityID.toString()
//
//        requestManager.request(request, userManager.user) { result, json ->
//            when (result) {
//                RequestResult.nSuccess -> {
//                    runOnUiThread { isLoading(false) }
//                    return@request
//                }
//                RequestResult.pSuccess -> {
//                    runOnUiThread { isLoading(false) }
//                    Log.d(TAG, "${json}")
//                    check(json != null) {
//                        Log.d(TAG, "json is null")
//                        return@request
//                    }
//                    when (result.statusCode) {
//                        200 -> {
//                            val item =
//                                json["relatedPlates"] as? ArrayList<LinkedTreeMap<String, Any>>
//                            val data = item?.map { Plate.create(it) } as ArrayList<Plate>
//
//                            runOnUiThread { plateAdapter.submitList(data) }
//
//                        }
//                    }
//                }
//                RequestResult.failure -> {
//                    runOnUiThread { isLoading(false) }
//                    root.snackbar("인터넷 연결 확인이 필요합니다")
//                }
//                else -> {
//                    runOnUiThread { isLoading(false) }
//                    root.snackbar("서버 상태가 좋지 않습니다")
//                }
//            }
//
//        }
//    }
//
//    private val mMessageReceiver: BroadcastReceiver =
//        object : BroadcastReceiver() {
//            override fun onReceive(context: Context?, intent: Intent) {
//                val num = intent.getIntExtra("status", 0)
//                allHide()
//
//                if (num != 0) {
//                    showNetworkFailure()
//                } else {
//                    hideNetworkFailure()
//                }
//            }
//        }
//}
