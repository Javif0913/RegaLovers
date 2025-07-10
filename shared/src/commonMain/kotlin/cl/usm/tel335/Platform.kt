package cl.usm.tel335

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform