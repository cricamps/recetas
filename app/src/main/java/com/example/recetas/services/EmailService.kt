package com.example.recetas.services

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

object EmailService {
    
    private const val MODO_DESARROLLO = true
    
    suspend fun enviarEmailBienvenida(nombre: String, email: String): Boolean {
        return withContext(Dispatchers.IO) {
            delay(1000)
            
            if (MODO_DESARROLLO) {
                logEmailBienvenida(nombre, email)
                true
            } else {
                enviarEmailProduccion(email, nombre)
            }
        }
    }
    
    suspend fun enviarEmailRecuperacion(email: String, codigo: String): Boolean {
        return withContext(Dispatchers.IO) {
            delay(1000)
            
            if (MODO_DESARROLLO) {
                logEmailRecuperacion(email, codigo)
                true
            } else {
                enviarEmailRecuperacionProduccion(email, codigo)
            }
        }
    }
    
    fun validarEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        return emailRegex.matches(email)
    }
    
    private fun logEmailBienvenida(nombre: String, email: String) {
        val template = generarTemplateTextoPlano(nombre)
        
        Log.d("EmailService", "═══════════════════════════════════════════════")
        Log.d("EmailService", "📧 EMAIL DE BIENVENIDA ENVIADO (MODO DESARROLLO)")
        Log.d("EmailService", "═══════════════════════════════════════════════")
        Log.d("EmailService", "Para: $email")
        Log.d("EmailService", "Nombre: $nombre")
        Log.d("EmailService", "Asunto: ¡Bienvenido a Recetas Chilenas!")
        Log.d("EmailService", "")
        Log.d("EmailService", "Contenido del Email:")
        Log.d("EmailService", template)
        Log.d("EmailService", "═══════════════════════════════════════════════")
    }
    
    private fun logEmailRecuperacion(email: String, codigo: String) {
        Log.d("EmailService", "═══════════════════════════════════════════════")
        Log.d("EmailService", "🔐 EMAIL DE RECUPERACIÓN ENVIADO (MODO DESARROLLO)")
        Log.d("EmailService", "═══════════════════════════════════════════════")
        Log.d("EmailService", "Para: $email")
        Log.d("EmailService", "Código: $codigo")
        Log.d("EmailService", "Asunto: Recupera tu contraseña - Recetas Chilenas")
        Log.d("EmailService", "═══════════════════════════════════════════════")
    }
    
    private fun generarTemplateHTML(nombre: String): String {
        return """
            <!DOCTYPE html>
            <html lang="es">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Bienvenido a Recetas Chilenas</title>
                <style>
                    body { 
                        margin: 0; 
                        padding: 0; 
                        font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, sans-serif;
                        background-color: #FFF8E1;
                    }
                    .container { 
                        max-width: 600px; 
                        margin: 40px auto; 
                        background: white; 
                        border-radius: 20px; 
                        padding: 40px;
                        box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
                    }
                    .logo { 
                        text-align: center; 
                        font-size: 80px; 
                        margin-bottom: 20px;
                    }
                    .app-name { 
                        color: #D32F2F; 
                        font-size: 32px; 
                        font-weight: bold; 
                        text-align: center;
                        margin: 0 0 10px 0;
                    }
                    .subtitle {
                        text-align: center;
                        color: #666;
                        font-size: 16px;
                        margin-bottom: 30px;
                    }
                    .greeting { 
                        font-size: 24px; 
                        color: #333; 
                        margin: 30px 0 20px 0;
                        text-align: center;
                    }
                    .welcome-text {
                        font-size: 18px;
                        color: #555;
                        line-height: 1.6;
                        text-align: center;
                        margin-bottom: 30px;
                    }
                    .features { 
                        background: #FFF8E1; 
                        padding: 30px; 
                        border-radius: 15px; 
                        margin: 30px 0;
                    }
                    .feature-title {
                        font-size: 20px;
                        font-weight: bold;
                        color: #D32F2F;
                        margin-bottom: 20px;
                        text-align: center;
                    }
                    .feature { 
                        padding: 15px 0; 
                        border-bottom: 1px solid #FFE4B5;
                        font-size: 16px;
                        color: #333;
                    }
                    .feature:last-child {
                        border-bottom: none;
                    }
                    .feature-icon {
                        font-size: 24px;
                        margin-right: 10px;
                    }
                    .cta-button { 
                        display: block;
                        background: #D32F2F; 
                        color: white; 
                        padding: 16px 32px; 
                        border-radius: 12px; 
                        text-decoration: none; 
                        font-weight: bold; 
                        font-size: 18px;
                        text-align: center;
                        margin: 30px auto;
                        max-width: 250px;
                    }
                    .footer { 
                        text-align: center; 
                        color: #999; 
                        font-size: 14px; 
                        margin-top: 40px;
                        padding-top: 30px;
                        border-top: 1px solid #eee;
                    }
                    .social-links {
                        margin: 20px 0;
                    }
                    .social-link {
                        display: inline-block;
                        margin: 0 10px;
                        font-size: 24px;
                        text-decoration: none;
                    }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="logo">🍲</div>
                    <h1 class="app-name">Recetas Chilenas</h1>
                    <p class="subtitle">Sabores tradicionales de Chile</p>
                    
                    <h2 class="greeting">¡Hola, $nombre! 👋</h2>
                    
                    <p class="welcome-text">
                        ¡Bienvenido a Recetas Chilenas! Estamos emocionados de tenerte en nuestra comunidad.
                    </p>
                    
                    <div class="features">
                        <div class="feature-title">✨ Lo que puedes hacer:</div>
                        
                        <div class="feature">
                            <span class="feature-icon">🥘</span>
                            <strong>33+ Recetas Tradicionales</strong><br>
                            Pastel de choclo, charquicán, cazuela y más
                        </div>
                        
                        <div class="feature">
                            <span class="feature-icon">🎙️</span>
                            <strong>Navegación por Voz</strong><br>
                            15+ comandos en español chileno
                        </div>
                        
                        <div class="feature">
                            <span class="feature-icon">♿</span>
                            <strong>100% Accesible</strong><br>
                            TalkBack, alto contraste, escalado de fuente
                        </div>
                        
                        <div class="feature">
                            <span class="feature-icon">📱</span>
                            <strong>Funciona Offline</strong><br>
                            Accede a tus recetas favoritas sin internet
                        </div>
                    </div>
                    
                    <a href="#" class="cta-button">🍽️ Explorar Recetas</a>
                    
                    <div class="footer">
                        <div class="social-links">
                            <a href="#" class="social-link">📘</a>
                            <a href="#" class="social-link">📷</a>
                            <a href="#" class="social-link">🐦</a>
                        </div>
                        <p>© 2026 Recetas Chilenas</p>
                        <p>Desarrollado con ❤️ para preservar la cocina tradicional chilena</p>
                    </div>
                </div>
            </body>
            </html>
        """.trimIndent()
    }
    
    private fun generarTemplateTextoPlano(nombre: String): String {
        return """
            ═══════════════════════════════════════════
            
            🍲 RECETAS CHILENAS
            Sabores tradicionales de Chile
            
            ═══════════════════════════════════════════
            
            ¡Hola, $nombre! 👋
            
            ¡Bienvenido a Recetas Chilenas! Estamos emocionados 
            de tenerte en nuestra comunidad.
            
            ✨ LO QUE PUEDES HACER:
            
            🥘 33+ Recetas Tradicionales
               Pastel de choclo, charquicán, cazuela y más
            
            🎙️ Navegación por Voz
               15+ comandos en español chileno
            
            ♿ 100% Accesible
               TalkBack, alto contraste, escalado de fuente
            
            📱 Funciona Offline
               Accede a tus recetas favoritas sin internet
            
            ═══════════════════════════════════════════
            
            © 2026 Recetas Chilenas
            Desarrollado con ❤️ para preservar 
            la cocina tradicional chilena
            
            ═══════════════════════════════════════════
        """.trimIndent()
    }
    
    private fun enviarEmailProduccion(email: String, nombre: String): Boolean {
        return false
    }
    
    private fun enviarEmailRecuperacionProduccion(email: String, codigo: String): Boolean {
        return false
    }
}
