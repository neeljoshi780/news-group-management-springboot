// Sidebar Toggle
const sidebar = document.querySelector(".sidebar");
const toggleBtn = document.getElementById("menu-toggle");
const closeBtn = document.getElementById("sidebar-close");

toggleBtn.addEventListener("click", () => {
    sidebar.classList.toggle("active");
});

closeBtn.addEventListener("click", () => {
    sidebar.classList.remove("active");
});

// Close sidebar on outside click (small screens)
document.addEventListener("click", (e) => {
    if (
        window.innerWidth <= 768 &&
        !sidebar.contains(e.target) &&
        !toggleBtn.contains(e.target) &&
        sidebar.classList.contains("active")
    ) {
        sidebar.classList.remove("active");
    }
});

// Traffic Chart
const ctx = document.getElementById("trafficChart").getContext("2d");
new Chart(ctx, {
    type: "line",
    data: {
        labels: [
            "Jan",
            "Feb",
            "Mar",
            "Apr",
            "May",
            "Jun",
            "Jul",
            "Aug",
            "Sep",
            "Oct",
            "Nov",
            "Dec",
        ],
        datasets: [
            {
                label: "Traffic",
                data: [
                    1200, 1900, 3000, 2500, 2200, 3100, 2800, 3200, 2700, 3500, 4000,
                    3800,
                ],
                backgroundColor: "rgba(78, 115, 223, 0.1)",
                borderColor: "#4e73df",
                borderWidth: 2,
                fill: true,
                tension: 0.4,
            },
        ],
    },
    options: {
        responsive: true,
        maintainAspectRatio: false,
        scales: {
            y: {
                beginAtZero: true,
                grid: { color: "#e2e8f0" },
                ticks: { color: "#718096" },
            },
            x: { grid: { display: false }, ticks: { color: "#718096" } },
        },
        plugins: {
            legend: { display: false },
            tooltip: { backgroundColor: "#2d3748" },
        },
    },
});