# Sarvath Dairy – upgrade notes

This update keeps the existing Spring Boot 3.2.4 / Java 17 / MySQL application and its static storefront.

## Added in this update
- Refreshed responsive storefront styling while keeping the existing product, cart and checkout flow.
- Product catalogue is loaded from the backend; featured products and display priority are respected.
- Product gallery supports multiple image URLs (comma-separated in the admin editor).
- Admin sign-in uses a short-lived email one-time code sent to the configured admin mailbox.
- Admin dashboard supports adding, editing, deleting, featuring and ordering products, plus order status updates for customer tracking.
- Admin dashboard can update contact phone/email, location, YouTube, WhatsApp/contact and app links, founder name/photo URL and logo URL.
- Orders are now persisted in `customer_orders`; customer order history is fetched from the backend.
- Delivery schedule is calculated to be at least eight hours after order placement and checked again by the backend.
- Customer logout clears local customer profile, customer ID, order-history cache and known IP-related local/session storage keys. Customer IDs created by the upgraded site use a longer random identifier. This app did not persist an IP address in the original source.

## Required environment variables
Configure these on your hosting provider; do not commit credentials:
- `DB_URL`
- `DB_USER`
- `DB_PASSWORD`
- `MAIL_USERNAME`
- `MAIL_PASSWORD` (Gmail app password if using Gmail SMTP)
- `BREVO_API_KEY`
- `ADMIN_EMAIL` (defaults to `sarvathdairy@gmail.com`)

The existing application already required the database, mail and Brevo variables. Admin OTP delivery additionally depends on working SMTP settings.

## Admin login
1. Open the website footer and choose **Admin**.
2. Enter the private admin email configured by `ADMIN_EMAIL`.
3. Choose **Send sign-in code**, then enter the six-digit code received by email.
4. The admin session expires after eight hours or when you sign out.

For security, this implementation sends a one-time sign-in code rather than emailing a permanent password. The code and admin session are held in application memory, so restarting the backend invalidates active sessions and pending codes.

## Product image galleries
Set the main image URL and enter additional image URLs separated by commas. Example:
`https://example.com/front.jpg, https://example.com/side.jpg`

## Deployment / verification
- The frontend currently targets `https://sarvath-milk.onrender.com` in production and `http://localhost:8080` on localhost. Change `API_BASE` in `src/main/resources/static/index.html` if your backend has a different production URL.
- Hibernate uses `ddl-auto=update`, so the new `customer_orders` and `site_settings` tables and additional product columns are created/updated when the app starts.
- I checked the updated inline JavaScript with Node.js syntax validation. Maven is not installed in this environment, so a full Java/Maven build and live database/email test could not be run.
- Before going live, configure HTTPS, restrict CORS to your actual storefront origin, add rate limiting to the OTP endpoint, and test SMTP/database connectivity. The current admin token store is in-memory and is suitable as a starter implementation; use a persistent session store and stronger production security controls for a high-traffic deployment.
