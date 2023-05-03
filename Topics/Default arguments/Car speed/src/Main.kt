fun checkSpeed(speed: Int, limit: Int = 60) = print(
    if (speed <= limit) {
        "Within the limit"
    } else {
        "Exceeds the limit by ${speed - limit} kilometers per hour"
    }
)
