package com.nutrizulia.common.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmailService {

    private final JavaMailSender mailSender;


    public void enviarCorreoHtml(String destinatario, String asunto, String htmlContenido) throws MessagingException {
        MimeMessage mensaje = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mensaje, true);
        helper.setTo(destinatario);
        helper.setSubject(asunto);
        helper.setText(htmlContenido, true);

        mailSender.send(mensaje);
    }

    public void recuperacionClave(String destinatario, String nombreUsuario, String clave) throws MessagingException {
        String asunto = "Recuperación de Contraseña - Nutrizulia";
        String htmlContenido = construirHtmlRecuperacionClave(nombreUsuario, clave);
        
        log.info("Enviando correo de recuperación a: {}", destinatario);
        log.debug("Asunto del correo: {}", asunto);
        log.debug("Clave: {}", clave);
        
        try {
            enviarCorreoHtml(destinatario, asunto, htmlContenido);
            log.info("Correo de recuperación enviado exitosamente a: {}", destinatario);
        } catch (MessagingException e) {
            log.error("Error detallado al enviar correo a {}: {}", destinatario, e.getMessage());
            log.error("Causa raíz: {}", e.getCause() != null ? e.getCause().getMessage() : "No disponible");
            throw e;
        }
    }

    public void creacionUsuario(String email, String nombreUsuario, String clave) {
        try {
            String htmlContent = construirHtmlCreacionUsuario(nombreUsuario, clave);
            enviarCorreoHtml(email, "Bienvenido a NutriZulia - Tu cuenta ha sido creada", htmlContent);
            log.info("Correo de creación de usuario enviado exitosamente a: {}", email);
        } catch (Exception e) {
            log.error("Error al enviar correo de creación de usuario a {}: {}", email, e.getMessage());
            throw new RuntimeException("Error al enviar correo de creación de usuario", e);
        }
    }

    private String construirHtmlRecuperacionClave(String nombreUsuario, String clave) {
        String htmlTemplate = """
                <!DOCTYPE html>
                <html lang="es">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Recuperación de Contraseña - NutriZulia</title>
                    <style>
                        body {
                            margin: 0;
                            padding: 0;
                            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                            background-color: #f5f7fa;
                            line-height: 1.6;
                        }
                        .container {
                            max-width: 600px;
                            margin: 0 auto;
                            background-color: #ffffff;
                            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
                            border-radius: 12px;
                            overflow: hidden;
                        }
                        .header {
                            background: linear-gradient(135deg, #1e40af 0%, #3b82f6 100%);
                            padding: 40px 30px;
                            text-align: center;
                            color: white;
                        }
                        .logo {
                            width: 80px;
                            height: 80px;
                            margin: 0 auto 20px;
                            background-color: rgba(255, 255, 255, 0.2);
                            border-radius: 50%;
                            display: flex;
                            align-items: center;
                            justify-content: center;
                            font-size: 36px;
                            font-weight: bold;
                        }
                        .header h1 {
                            margin: 0;
                            font-size: 28px;
                            font-weight: 600;
                            letter-spacing: -0.5px;
                        }
                        .header p {
                            margin: 10px 0 0;
                            font-size: 16px;
                            opacity: 0.9;
                        }
                        .content {
                            padding: 40px 30px;
                        }
                        .greeting {
                            font-size: 20px;
                            color: #1e40af;
                            margin-bottom: 20px;
                            font-weight: 600;
                        }
                        .message {
                            color: #4b5563;
                            font-size: 16px;
                            margin-bottom: 30px;
                            line-height: 1.7;
                        }
                        .password-container {
                            background: linear-gradient(135deg, #eff6ff 0%, #dbeafe 100%);
                            border: 2px solid #3b82f6;
                            border-radius: 12px;
                            padding: 25px;
                            text-align: center;
                            margin: 30px 0;
                        }
                        .password-label {
                            color: #1e40af;
                            font-size: 14px;
                            font-weight: 600;
                            text-transform: uppercase;
                            letter-spacing: 1px;
                            margin-bottom: 10px;
                        }
                        .password {
                            font-size: 24px;
                            font-weight: bold;
                            color: #1e40af;
                            font-family: 'Courier New', monospace;
                            letter-spacing: 2px;
                            background-color: white;
                            padding: 15px 20px;
                            border-radius: 8px;
                            border: 1px solid #93c5fd;
                            display: inline-block;
                            margin: 10px 0;
                        }
                        .instructions {
                            background-color: #fef3c7;
                            border-left: 4px solid #f59e0b;
                            padding: 20px;
                            margin: 30px 0;
                            border-radius: 0 8px 8px 0;
                        }
                        .instructions h3 {
                            color: #92400e;
                            margin: 0 0 10px;
                            font-size: 16px;
                        }
                        .instructions p {
                            color: #78350f;
                            margin: 0;
                            font-size: 14px;
                        }
                        .footer {
                            background-color: #f8fafc;
                            padding: 30px;
                            text-align: center;
                            border-top: 1px solid #e5e7eb;
                        }
                        .footer p {
                            color: #6b7280;
                            font-size: 14px;
                            margin: 5px 0;
                        }
                        .footer .company {
                            color: #1e40af;
                            font-weight: 600;
                            font-size: 16px;
                        }
                        .security-note {
                            background-color: #fef2f2;
                            border: 1px solid #fecaca;
                            border-radius: 8px;
                            padding: 15px;
                            margin: 20px 0;
                        }
                        .security-note p {
                            color: #dc2626;
                            font-size: 13px;
                            margin: 0;
                            font-weight: 500;
                        }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="header">
                            <h1>NutriZulia</h1>
                            <p>Sistema de Gestión Nutricional</p>
                        </div>
                        
                        <div class="content">
                            <div class="greeting">¡Hola, NOMBRE_USUARIO!</div>
                            
                            <div class="message">
                                Hemos recibido una solicitud para restablecer tu contraseña. 
                                Tu nueva contraseña temporal ha sido generada exitosamente.
                            </div>
                            
                            <div class="password-container">
                                <div class="password-label">Tu contraseña temporal</div>
                                <div class="password">CLAVE_TEMPORAL</div>
                            </div>
                            
                            <div class="instructions">
                                <h3>Instrucciones importantes:</h3>
                                <ul>
                                 <li>Utiliza esta contraseña temporal para acceder a tu cuenta</li>
                                 <li>Te recomendamos cambiar esta contraseña por una personalizada una vez que ingreses</li>
                                 <li>Esta contraseña es válida y segura para tu uso inmediato</li>
                                </ul>
                            </div>
                            
                            <div class="security-note">
                                <p>
                                    Por tu seguridad: Si no solicitaste este cambio de contraseña, 
                                    contacta inmediatamente con nuestro equipo de soporte.
                                </p>
                            </div>
                        </div>
                        
                        <div class="footer">
                            <p class="company">NutriZulia</p>
                            <p>Sistema de Gestión Nutricional Profesional</p>
                            <p>Este es un correo automático, por favor no responder.</p>
                        </div>
                    </div>
                </body>
                </html>
                """;
        
        return htmlTemplate
                .replace("NOMBRE_USUARIO", nombreUsuario)
                .replace("CLAVE_TEMPORAL", clave);
    }

    private String construirHtmlCreacionUsuario(String nombreUsuario, String clave) {
        String htmlTemplate = """
                <!DOCTYPE html>
                <html lang="es">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Bienvenido a NutriZulia</title>
                    <style>
                        body {
                            margin: 0;
                            padding: 0;
                            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                            background-color: #f5f7fa;
                            line-height: 1.6;
                        }
                        .container {
                            max-width: 600px;
                            margin: 0 auto;
                            background-color: #ffffff;
                            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
                            border-radius: 12px;
                            overflow: hidden;
                        }
                        .header {
                            background: linear-gradient(135deg, #059669 0%, #10b981 100%);
                            padding: 40px 30px;
                            text-align: center;
                            color: white;
                        }
                        .logo {
                            width: 80px;
                            height: 80px;
                            margin: 0 auto 20px;
                            background-color: rgba(255, 255, 255, 0.2);
                            border-radius: 50%;
                            display: flex;
                            align-items: center;
                            justify-content: center;
                            font-size: 36px;
                            font-weight: bold;
                        }
                        .header h1 {
                            margin: 0;
                            font-size: 28px;
                            font-weight: 600;
                            letter-spacing: -0.5px;
                        }
                        .header p {
                            margin: 10px 0 0;
                            font-size: 16px;
                            opacity: 0.9;
                        }
                        .content {
                            padding: 40px 30px;
                        }
                        .greeting {
                            font-size: 20px;
                            color: #059669;
                            margin-bottom: 20px;
                            font-weight: 600;
                        }
                        .message {
                            color: #4b5563;
                            font-size: 16px;
                            margin-bottom: 30px;
                            line-height: 1.7;
                        }
                        .welcome-message {
                            background: linear-gradient(135deg, #ecfdf5 0%, #d1fae5 100%);
                            border: 2px solid #10b981;
                            border-radius: 12px;
                            padding: 25px;
                            text-align: center;
                            margin: 30px 0;
                        }
                        .welcome-title {
                            color: #059669;
                            font-size: 18px;
                            font-weight: 600;
                            margin-bottom: 15px;
                        }
                        .password-container {
                            background: linear-gradient(135deg, #eff6ff 0%, #dbeafe 100%);
                            border: 2px solid #3b82f6;
                            border-radius: 12px;
                            padding: 25px;
                            text-align: center;
                            margin: 30px 0;
                        }
                        .password-label {
                            color: #1e40af;
                            font-size: 14px;
                            font-weight: 600;
                            text-transform: uppercase;
                            letter-spacing: 1px;
                            margin-bottom: 10px;
                        }
                        .password {
                            font-size: 24px;
                            font-weight: bold;
                            color: #1e40af;
                            font-family: 'Courier New', monospace;
                            letter-spacing: 2px;
                            background-color: white;
                            padding: 15px 20px;
                            border-radius: 8px;
                            border: 1px solid #93c5fd;
                            display: inline-block;
                            margin: 10px 0;
                        }
                        .instructions {
                            background-color: #fef3c7;
                            border-left: 4px solid #f59e0b;
                            padding: 20px;
                            margin: 30px 0;
                            border-radius: 0 8px 8px 0;
                        }
                        .instructions h3 {
                            color: #92400e;
                            margin: 0 0 10px;
                            font-size: 16px;
                        }
                        .instructions p {
                            color: #78350f;
                            margin: 0;
                            font-size: 14px;
                        }
                        .footer {
                            background-color: #f8fafc;
                            padding: 30px;
                            text-align: center;
                            border-top: 1px solid #e5e7eb;
                        }
                        .footer p {
                            color: #6b7280;
                            font-size: 14px;
                            margin: 5px 0;
                        }
                        .footer .company {
                            color: #059669;
                            font-weight: 600;
                            font-size: 16px;
                        }
                        .security-note {
                            background-color: #f0f9ff;
                            border: 1px solid #7dd3fc;
                            border-radius: 8px;
                            padding: 15px;
                            margin: 20px 0;
                        }
                        .security-note p {
                            color: #0369a1;
                            font-size: 13px;
                            margin: 0;
                            font-weight: 500;
                        }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="header">
                            <h1>NutriZulia</h1>
                            <p>Sistema de Gestión Nutricional</p>
                        </div>
                        
                        <div class="content">
                            <div class="greeting">¡Bienvenido, NOMBRE_USUARIO!</div>
                            
                            <div class="welcome-message">
                                <div class="welcome-title">¡Tu cuenta ha sido creada exitosamente!</div>
                                <p>Nos complace informarte que tu cuenta en NutriZulia ha sido configurada y está lista para usar.</p>
                            </div>
                            
                            <div class="message">
                                Para garantizar la seguridad de tu cuenta, hemos generado una contraseña temporal que podrás usar para acceder al sistema por primera vez.
                            </div>
                            
                            <div class="password-container">
                                <div class="password-label">Tu contraseña temporal</div>
                                <div class="password">CLAVE_TEMPORAL</div>
                            </div>
                            
                            <div class="instructions">
                                <h3>Próximos pasos:</h3>
                                <ul>
                                 <li>Utiliza esta contraseña temporal para acceder a tu cuenta</li>
                                 <li>Una vez dentro del sistema, te recomendamos cambiar esta contraseña por una personalizada</li>
                                 <li>Explora todas las funcionalidades que NutriZulia tiene para ofrecerte</li>
                                 <li>Si tienes alguna pregunta, no dudes en contactar a nuestro equipo de soporte</li>
                                </ul>
                            </div>
                            
                            <div class="security-note">
                                <p>
                                    Por tu seguridad: Mantén esta contraseña en un lugar seguro y cámbiala tan pronto como accedas a tu cuenta.
                                </p>
                            </div>
                        </div>
                        
                        <div class="footer">
                            <p class="company">NutriZulia</p>
                            <p>Sistema de Gestión Nutricional Profesional</p>
                            <p>¡Gracias por confiar en nosotros!</p>
                            <p>Este es un correo automático, por favor no responder.</p>
                        </div>
                    </div>
                </body>
                </html>
                """;
        
        return htmlTemplate
                .replace("NOMBRE_USUARIO", nombreUsuario)
                .replace("CLAVE_TEMPORAL", clave);
    }

}