package com.example.data

import java.util.UUID

enum class UserRole {
    ADMIN, MANAGER, CREATOR, CLIENT
}

enum class ContentStatus {
    DRAFT, EDITING, APPROVED, SCHEDULED, POSTED
}

enum class TaskStatus {
    TO_DO, IN_PROGRESS, REVIEW, APPROVED, COMPLETED
}

enum class TaskPriority {
    LOW, MEDIUM, HIGH
}

data class User(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val email: String,
    val role: UserRole,
    val avatarUrl: String
)

data class Client(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val industry: String,
    val status: String // "Active", "Onboarding", "Paused"
)

data class Invoice(
    val id: String = UUID.randomUUID().toString(),
    val clientId: String,
    val clientName: String, // Helper for easier UI display
    val amount: Double,
    val currency: String = "USD",
    val status: String, // "Paid", "Pending", "Overdue"
    val dueDate: String,
    val paidAt: String? = null
)

data class Expense(
    val id: String = UUID.randomUUID().toString(),
    val label: String,
    val category: String, // "SaaS Tools", "Freelance Crew", "Content Production", "Advertising", "Office & Travel"
    val amount: Double,
    val spentAt: String
)

data class ContentItem(
    val id: String = UUID.randomUUID().toString(),
    val clientName: String,
    val title: String,
    val format: String, // "Reels", "Carousels", "Static posts", "Stories", "Shorts", "Founder POV"
    val caption: String,
    val hashtags: String,
    val status: ContentStatus,
    val scheduledAt: String
)

data class Task(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val priority: TaskPriority,
    val status: TaskStatus,
    val dueDate: String,
    val ownerName: String,
    val clientName: String
)

data class AnalyticsSnapshot(
    val id: String = UUID.randomUUID().toString(),
    val platform: String, // "Instagram", "TikTok", "LinkedIn", "YouTube Shorts"
    val followers: Int,
    val likes: Int,
    val comments: Int,
    val shares: Int,
    val saves: Int,
    val reach: Int,
    val impressions: Int,
    val capturedAt: String
)

data class BusinessCardEvent(
    val id: String = UUID.randomUUID().toString(),
    val cardSlug: String,
    val eventType: String, // "View", "Save Contact", "WhatsApp Click", "Portfolio Click"
    val source: String, // "NFC Tag", "QR Code", "Direct Link"
    val metadata: String,
    val createdAt: String
)

data class TeamActivity(
    val id: String = UUID.randomUUID().toString(),
    val userName: String,
    val action: String,
    val target: String,
    val timeAgo: String
)

object SampleData {
    val users = listOf(
        User("u1", "Inderneil Kanagali", "inderneilkanagali@gmail.com", UserRole.ADMIN, "https://api.dicebear.com/7.x/adventurer/svg?seed=inderneil"),
        User("u2", "Sara Mitchell", "sara@thesocialink.com", UserRole.MANAGER, "https://api.dicebear.com/7.x/adventurer/svg?seed=sara"),
        User("u3", "Devansh Sharma", "dev@thesocialink.com", UserRole.CREATOR, "https://api.dicebear.com/7.x/adventurer/svg?seed=dev"),
        User("u4", "Alex Mercer", "alex@fintechretainer.com", UserRole.CLIENT, "https://api.dicebear.com/7.x/adventurer/svg?seed=alex")
    )

    val clients = listOf(
        Client("c1", "Fintech Retainer", "FinTech", "Active"),
        Client("c2", "D2C Aura Brand", "E-Commerce", "Active"),
        Client("c3", "Luxury Retail Co", "Luxe Apparel", "Active"),
        Client("c4", "Hospitality Group", "Hotels & Food", "Onboarding"),
        Client("c5", "EdTech Studio", "Education", "Active"),
        Client("c6", "Wellness Clinic", "Healthcare", "Active")
    )

    val invoices = listOf(
        Invoice("i1", "c1", "Fintech Retainer", 8500.00, "USD", "Paid", "2026-05-15", "2026-05-14"),
        Invoice("i2", "c2", "D2C Aura Brand", 4200.00, "USD", "Paid", "2026-05-20", "2026-05-20"),
        Invoice("i3", "c3", "Luxury Retail Co", 12500.00, "USD", "Pending", "2026-06-05"),
        Invoice("i4", "c5", "EdTech Studio", 5000.00, "USD", "Paid", "2026-05-01", "2026-05-01"),
        Invoice("i5", "c6", "Wellness Clinic", 3500.00, "USD", "Overdue", "2026-05-10"),
        Invoice("i6", "c1", "Fintech Retainer", 8500.00, "USD", "Pending", "2026-06-15"),
        Invoice("i7", "c4", "Hospitality Group", 6000.00, "USD", "Pending", "2026-06-01")
    )

    val expenses = listOf(
        Expense("e1", "Framer Pro & Figma Team", "SaaS Tools", 320.0, "2026-05-02"),
        Expense("e2", "Cinematographer Retainer - Carousel Shoot", "Freelance Crew", 1500.0, "2026-05-11"),
        Expense("e3", "Studio Room Booking (Aesthetic Loft)", "Content Production", 450.0, "2026-05-12"),
        Expense("e4", "Meta Ads Campaign (Luxury Client)", "Advertising", 2500.0, "2026-05-15"),
        Expense("e5", "Travel to Founder interview Delhi", "Office & Travel", 680.0, "2026-05-18"),
        Expense("e6", "CapCut Pro & Midjourney subscriptions", "SaaS Tools", 75.0, "2026-05-22")
    )

    val contentPipeline = listOf(
        ContentItem("cp1", "Fintech Retainer", "3 Mistakes in Crypto Investing", "Reels", "These 3 mistakes could easily lose you thousands of dollars in the next bull run. Watch till the end to save your capital! 📈💡\n\n#crypto #investing #finance #reelsinstagram", "#finance #investing #wealth #crypto", ContentStatus.APPROVED, "2026-05-27 18:00"),
        ContentItem("cp2", "D2C Aura Brand", "Visual Walkthrough - Aura Serum", "Reels", "The texture of our new Aura Serum is pure luxury. Designed to absorb deeply and restore a 24-hour glow. Glow with intent. 💧✨\n\n#aura #skincare #d2c #luxury #aesthetic", "#skincare #glowingskin #aesthetic #glow", ContentStatus.SCHEDULED, "2026-05-28 17:00"),
        ContentItem("cp3", "Luxury Retail Co", "Modern Minimalism Visual Lookbook", "Carousels", "A curated slide deck exploring the architecture of modern minimal wear. Texture, structure, asymmetry.", "#minimalism #streetwear #luxuryfashion", ContentStatus.EDITING, "2026-05-30 19:30"),
        ContentItem("cp4", "Hospitality Group", "Hidden Roof Deck Cocktail Hour", "Stories", "Spontaneous vibe from our luxury lounge deck. Exclusive invite link in bio.", "#rooftop #mixology #luxelifestyle", ContentStatus.DRAFT, "2026-05-26 21:00"),
        ContentItem("cp5", "EdTech Studio", "Founder POV: Scaling to $10M ARR", "Founder POV", "Why we stopped hiring junior developers and shifted entire team budgets to AI pair programmers. A provocative narrative on performance.", "#startup #founder #ai #saas", ContentStatus.POSTED, "2026-05-24 15:00"),
        ContentItem("cp6", "Wellness Clinic", "The Truth Behind Sleeping Pills", "Static posts", "Chronic insomnia isn't solved in a pharmacy. Let's talk standard circadian re-alignment steps.", "#wellness #insomnia #sleephealth", ContentStatus.APPROVED, "2026-05-29 10:00")
    )

    val tasks = listOf(
        Task("t1", "Execute Meta Retargeting Ad Sets", TaskPriority.HIGH, TaskStatus.IN_PROGRESS, "2026-05-28", "Sara Mitchell", "Fintech Retainer"),
        Task("t2", "Edit Aura Serum Reel - Speed Ramps & SFX", TaskPriority.MEDIUM, TaskStatus.REVIEW, "2026-05-27", "Devansh Sharma", "D2C Aura Brand"),
        Task("t3", "Shoot Editorial Lookbook Photoset", TaskPriority.HIGH, TaskStatus.TO_DO, "2026-05-29", "Devansh Sharma", "Luxury Retail Co"),
        Task("t4", "Draft Proposal for Quarter 3 Campaign", TaskPriority.LOW, TaskStatus.TO_DO, "2026-06-02", "Sara Mitchell", "Hospitality Group"),
        Task("t5", "Approve EdTech Script copy", TaskPriority.MEDIUM, TaskStatus.APPROVED, "2026-05-26", "Inderneil Kanagali", "EdTech Studio"),
        Task("t6", "Schedule Instagram Calendar for Wellness", TaskPriority.LOW, TaskStatus.COMPLETED, "2026-05-25", "Sara Mitchell", "Wellness Clinic"),
        Task("t7", "Founder Video Setup - Dynamic Lighting Review", TaskPriority.MEDIUM, TaskStatus.IN_PROGRESS, "2026-05-28", "Devansh Sharma", "The Social INK")
    )

    val analyticsInstagram = AnalyticsSnapshot(
        platform = "Instagram",
        followers = 48500,
        likes = 142000,
        comments = 12400,
        shares = 35200,
        saves = 18400,
        reach = 185000,
        impressions = 450000,
        capturedAt = "May 2026"
    )

    val analyticsTikTok = AnalyticsSnapshot(
        platform = "TikTok",
        followers = 124000,
        likes = 852000,
        comments = 41200,
        shares = 94300,
        saves = 76000,
        reach = 1120000,
        impressions = 2400000,
        capturedAt = "May 2026"
    )

    val analyticsLinkedIn = AnalyticsSnapshot(
        platform = "LinkedIn",
        followers = 18200,
        likes = 12500,
        comments = 2300,
        shares = 4500,
        saves = 3100,
        reach = 94000,
        impressions = 155000,
        capturedAt = "May 2026"
    )

    val activities = listOf(
        TeamActivity("a1", "Sara Mitchell", "approved caption of Reel", "Fintech Retainer", "2 mins ago"),
        TeamActivity("a2", "Devansh Sharma", "uploaded editing draft for Reel v2", "D2C Aura Brand", "15 mins ago"),
        TeamActivity("a3", "Inderneil Kanagali", "signed client proposal on Retainer", "Hospitality Group", "1 hour ago"),
        TeamActivity("a4", "Sara Mitchell", "created task 'Shoot Editorial Lookbook'", "Luxury Retail Co", "3 hours ago"),
        TeamActivity("a5", "System Automated", "captured Instagram weekly snapshot metrics", "INKco SaaS", "12 hours ago")
    )

    val businessCardEvents = listOf(
        BusinessCardEvent("be1", "inderneil", "View", "QR Code", "iOS, Safari, Mumbai", "2026-05-26 09:30"),
        BusinessCardEvent("be2", "inderneil", "Save Contact", "NFC Tag", "Android, Chrome, Bangalore", "2026-05-25 18:22"),
        BusinessCardEvent("be3", "inderneil", "WhatsApp Click", "Direct Link", "macOS, Chrome, London", "2026-05-25 15:40"),
        BusinessCardEvent("be4", "inderneil", "Portfolio Click", "QR Code", "iOS, Safari, Dubai", "2026-05-24 11:15")
    )
}
