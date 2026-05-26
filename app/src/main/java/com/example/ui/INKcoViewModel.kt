package com.example.ui

import androidx.lifecycle.ViewModel
import com.example.data.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class Screen {
    LOGIN, SIGNUP, DASHBOARD, COMPANY, BUSINESS_CARD, REVENUE, CONTENT, TASKS, ANALYTICS, SETTINGS
}

class INKcoViewModel : ViewModel() {

    // Authentication and Role states
    private val _currentUser = MutableStateFlow<User?>(SampleData.users.first()) // Inderneil logged in by default
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _userRole = MutableStateFlow(UserRole.ADMIN)
    val userRole: StateFlow<UserRole> = _userRole.asStateFlow()

    private val _currentScreen = MutableStateFlow(Screen.DASHBOARD)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    // Interactive State lists
    private val _invoices = MutableStateFlow<List<Invoice>>(SampleData.invoices)
    val invoices: StateFlow<List<Invoice>> = _invoices.asStateFlow()

    private val _expenses = MutableStateFlow<List<Expense>>(SampleData.expenses)
    val expenses: StateFlow<List<Expense>> = _expenses.asStateFlow()

    private val _contentItems = MutableStateFlow<List<ContentItem>>(SampleData.contentPipeline)
    val contentItems: StateFlow<List<ContentItem>> = _contentItems.asStateFlow()

    private val _tasks = MutableStateFlow<List<Task>>(SampleData.tasks)
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    private val _clients = MutableStateFlow<List<Client>>(SampleData.clients)
    val clients: StateFlow<List<Client>> = _clients.asStateFlow()

    private val _activities = MutableStateFlow<List<TeamActivity>>(SampleData.activities)
    val activities: StateFlow<List<TeamActivity>> = _activities.asStateFlow()

    private val _businessCardEvents = MutableStateFlow<List<BusinessCardEvent>>(SampleData.businessCardEvents)
    val businessCardEvents: StateFlow<List<BusinessCardEvent>> = _businessCardEvents.asStateFlow()

    // Editable Company Deck States
    private val _companyName = MutableStateFlow("THE SOCIAL INK")
    val companyName: StateFlow<String> = _companyName.asStateFlow()

    private val _companySubtitle = MutableStateFlow("SOCIAL-FIRST BRAND SYSTEMS & CONTENT ENGINES")
    val companySubtitle: StateFlow<String> = _companySubtitle.asStateFlow()

    private val _companyImpressions = MutableStateFlow("18M+")
    val companyImpressions: StateFlow<String> = _companyImpressions.asStateFlow()

    private val _companyRevenueGenerated = MutableStateFlow("₹2.5M+")
    val companyRevenueGenerated: StateFlow<String> = _companyRevenueGenerated.asStateFlow()

    private val _companyRetainerRate = MutableStateFlow("94%")
    val companyRetainerRate: StateFlow<String> = _companyRetainerRate.asStateFlow()

    private val _companyManifesto = MutableStateFlow("We reject cookie-cutter marketing strategies. The Social INK builds social-first brand configurations, engaging cinematic content pipelines, and high-conversion campaign systems that enable ambitious companies to command absolute market share.")
    val companyManifesto: StateFlow<String> = _companyManifesto.asStateFlow()

    // Command Bar search query
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Notification states
    private val _notifications = MutableStateFlow<List<String>>(
        listOf(
            "Fintech Retainer just deposited ₹8,500.00 retainer.",
            "Instagram reel '3 Mistakes in Crypto' was approved by Inderneil Kanagali.",
            "Client 'Wellness Clinic' invoice of ₹3,500 is now overdue."
        )
    )
    val notifications: StateFlow<List<String>> = _notifications.asStateFlow()

    // Theme Customizer
    private val _glassmorphismEnabled = MutableStateFlow(true)
    val glassmorphismEnabled: StateFlow<Boolean> = _glassmorphismEnabled.asStateFlow()

    // Engagement Calculator State
    val calcFollowers = MutableStateFlow("48500")
    val calcLikes = MutableStateFlow("1200")
    val calcComments = MutableStateFlow("150")
    val calcShares = MutableStateFlow("80")
    val calcSaves = MutableStateFlow("110")
    val calcReach = MutableStateFlow("12000")
    val calcImpressions = MutableStateFlow("18200")

    fun navigateTo(screen: Screen) {
        _currentScreen.value = screen
    }

    fun logout() {
        _currentUser.value = null
        _currentScreen.value = Screen.LOGIN
    }

    fun login(email: String, name: String, role: UserRole) {
        val user = User(
            id = "u_" + System.currentTimeMillis(),
            name = name.ifBlank { "User Guest" },
            email = email.ifBlank { "guest@thesocialink.com" },
            role = role,
            avatarUrl = "https://api.dicebear.com/7.x/adventurer/svg?seed=" + name
        )
        _currentUser.value = user
        _userRole.value = role
        _currentScreen.value = Screen.DASHBOARD
        addActivity(user.name, "logged in to the operating system", "INKco Console")
    }

    fun changeUserRole(role: UserRole) {
        _userRole.value = role
        _currentUser.value = _currentUser.value?.copy(role = role)
        addActivity("System Automated", "updated role privileges to ${role.name}", "User Session")
    }

    fun addActivity(userName: String, action: String, target: String) {
        val newAct = TeamActivity(
            userName = userName,
            action = action,
            target = target,
            timeAgo = "Just now"
        )
        _activities.value = listOf(newAct) + _activities.value
    }

    fun addNotification(message: String) {
        _notifications.value = listOf(message) + _notifications.value
    }

    fun dismissNotification(index: Int) {
        val currentList = _notifications.value.toMutableList()
        if (index in currentList.indices) {
            currentList.removeAt(index)
            _notifications.value = currentList
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun toggleGlassmorphism() {
        _glassmorphismEnabled.value = !_glassmorphismEnabled.value
    }

    // Functions to update company specs directly from UI
    fun updateCompanySpecs(
        name: String,
        subtitle: String,
        impressions: String,
        revenue: String,
        retainer: String,
        manifesto: String
    ) {
        _companyName.value = name
        _companySubtitle.value = subtitle
        _companyImpressions.value = impressions
        _companyRevenueGenerated.value = revenue
        _companyRetainerRate.value = retainer
        _companyManifesto.value = manifesto
        addActivity(_currentUser.value?.name ?: "Founder", "updated agency business settings spec sheet", "Company Profile")
        addNotification("Company profile details and client metrics updated successfully.")
    }

    fun addClient(name: String, industry: String, status: String) {
        val newClient = Client(name = name, industry = industry, status = status)
        _clients.value = _clients.value + newClient
        addActivity(_currentUser.value?.name ?: "Founder", "registered new active retainer", name)
        addNotification("Retainer account created for $name ($industry).")
    }

    // Interactive Invoices manipulation
    fun createInvoice(clientName: String, amount: Double, dueDate: String) {
        val client = _clients.value.firstOrNull { it.name.equals(clientName, ignoreCase = true) }
        val clientId = client?.id ?: "c_misc"
        val newInvoice = Invoice(
            clientId = clientId,
            clientName = clientName,
            amount = amount,
            status = "Pending",
            dueDate = dueDate
        )
        _invoices.value = listOf(newInvoice) + _invoices.value
        addActivity(_currentUser.value?.name ?: "Admin", "drafted manual invoice of ₹${String.format("%.2f", amount)}", clientName)
        addNotification("New pending invoice of ₹${String.format("%.2f", amount)} created for $clientName.")
    }

    fun payInvoice(id: String) {
        _invoices.value = _invoices.value.map {
            if (it.id == id) {
                val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                it.copy(status = "Paid", paidAt = today)
            } else it
        }
        val invoice = _invoices.value.firstOrNull { it.id == id }
        invoice?.let {
            addActivity(_currentUser.value?.name ?: "Admin", "received payout approval for invoice", it.clientName)
            addNotification("Payment of ₹${String.format("%.2f", it.amount)} marked complete from ${it.clientName}.")
        }
    }

    // Interactive Expenses manipulation
    fun addExpense(label: String, category: String, amount: Double) {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val newExpense = Expense(
            label = label,
            category = category,
            amount = amount,
            spentAt = today
        )
        _expenses.value = listOf(newExpense) + _expenses.value
        addActivity(_currentUser.value?.name ?: "Admin", "authorized operational expense '$label'", "₹$amount")
    }

    // Interactive Content manipulation
    fun createContent(clientName: String, title: String, format: String, caption: String, hashtags: String, status: ContentStatus = ContentStatus.DRAFT) {
        val newPost = ContentItem(
            clientName = clientName,
            title = title,
            format = format,
            caption = caption,
            hashtags = hashtags,
            status = status,
            scheduledAt = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())
        )
        _contentItems.value = listOf(newPost) + _contentItems.value
        addActivity(_currentUser.value?.name ?: "Creator", "drafted new creative asset '$title' in stage ${status.name}", clientName)
    }

    fun updateContentStatus(id: String, newStatus: ContentStatus) {
        _contentItems.value = _contentItems.value.map {
            if (it.id == id) {
                it.copy(status = newStatus)
            } else it
        }
        val p = _contentItems.value.firstOrNull { it.id == id }
        p?.let {
            addActivity(_currentUser.value?.name ?: "Manager", "shifted status of '${p.title}' to ${newStatus.name}", p.clientName)
            addNotification("Asset pipeline updated: '${p.title}' is now ${newStatus.name}.")
        }
    }

    // Interactive Tasks manipulation
    fun createTask(title: String, priority: TaskPriority, clientName: String, ownerName: String = "Devansh Sharma", status: TaskStatus = TaskStatus.TO_DO) {
        val newTask = Task(
            title = title,
            priority = priority,
            status = status,
            dueDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(System.currentTimeMillis() + 86400000 * 3)),
            ownerName = ownerName,
            clientName = clientName
        )
        _tasks.value = listOf(newTask) + _tasks.value
        addActivity(_currentUser.value?.name ?: "Coordinator", "delegated task to $ownerName in stage ${status.name.replace("_", " ")}", clientName)
        addNotification("Task task priority [${priority.name}] delegated: $title.")
    }

    fun updateTaskStatus(id: String, newStatus: TaskStatus) {
        _tasks.value = _tasks.value.map {
            if (it.id == id) {
                it.copy(status = newStatus)
            } else it
        }
        val t = _tasks.value.firstOrNull { it.id == id }
        t?.let {
            addActivity(_currentUser.value?.name ?: "Member", "moved task '${t.title}' to ${newStatus.name}", t.clientName)
        }
    }

    // Interactive Digital Business Card operations
    fun logBusinessCardEngagement(eventType: String, source: String = "Mobile Interconnect") {
        val today = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())
        val event = BusinessCardEvent(
            cardSlug = "inderneil",
            eventType = eventType,
            source = source,
            metadata = "Device Sim, Android SDK Preview",
            createdAt = today
        )
        _businessCardEvents.value = listOf(event) + _businessCardEvents.value
        addNotification("Founder card action logged: Inderneil Kanagali [$eventType].")
    }

    // Form-based metric calculator
    fun calculateStandardEngagement(): Double {
        val followers = calcFollowers.value.toDoubleOrNull() ?: 1.0
        val likes = calcLikes.value.toDoubleOrNull() ?: 0.0
        val comments = calcComments.value.toDoubleOrNull() ?: 0.0
        val shares = calcShares.value.toDoubleOrNull() ?: 0.0
        val saves = calcSaves.value.toDoubleOrNull() ?: 0.0
        if (followers <= 0) return 0.0
        return ((likes + comments + shares + saves) / followers) * 100.0
    }

    fun calculateReachEngagement(): Double {
        val reach = calcReach.value.toDoubleOrNull() ?: 1.0
        val likes = calcLikes.value.toDoubleOrNull() ?: 0.0
        val comments = calcComments.value.toDoubleOrNull() ?: 0.0
        val shares = calcShares.value.toDoubleOrNull() ?: 0.0
        val saves = calcSaves.value.toDoubleOrNull() ?: 0.0
        if (reach <= 0) return 0.0
        return ((likes + comments + shares + saves) / reach) * 100.0
    }

    fun calculateImpressionEngagement(): Double {
        val impressions = calcImpressions.value.toDoubleOrNull() ?: 1.0
        val likes = calcLikes.value.toDoubleOrNull() ?: 0.0
        val comments = calcComments.value.toDoubleOrNull() ?: 0.0
        val shares = calcShares.value.toDoubleOrNull() ?: 0.0
        val saves = calcSaves.value.toDoubleOrNull() ?: 0.0
        if (impressions <= 0) return 0.0
        return ((likes + comments + shares + saves) / impressions) * 100.0
    }
}
