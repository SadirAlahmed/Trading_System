# المرحلة الأولى: بناء المشروع وجلب جميع المكتبات الخارجية (Dependencies)
FROM maven:3.9.9-amazoncorretto-24 AS build
WORKDIR /app
COPY . .
# بناء المشروع + أمر ذكي لنسخ كافة المكتبات الخارجية لمجلد مستقل
RUN mvn clean package -DskipTests && mvn dependency:copy-dependencies -DoutputDirectory=target/dependency

# المرحلة الثانية: تشغيل المشروع
FROM amazoncorretto:24-alpine
WORKDIR /app
# نسخ ملف المشروع الرئيسي
COPY --from=build /app/target/*.jar app.jar
# نسخ مجلد المكتبات الخارجية الذي قمنا بتجميعه
COPY --from=build /app/target/dependency /app/dependency

EXPOSE 8080

# تشغيل التطبيق مع ربط ملفك الرئيسي بجميع المكتبات الخارجية المساعدة في الخلفية
CMD ["java", "-cp", "app.jar:dependency/*", "com.sadir.trading.Main"]
