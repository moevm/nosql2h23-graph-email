# Используйте образ с Android SDK
FROM openjdk:8-jdk

# Установите Android SDK и платформы
RUN apt-get update -qq \
    && apt-get install -y --no-install-recommends \
        wget \
        unzip \
    && rm -rf /var/lib/apt/lists/* \
    && wget -q https://dl.google.com/android/repository/sdk-tools-linux-4333796.zip -O android-sdk.zip \
    && unzip -q android-sdk.zip -d /usr/local/android-sdk \
    && rm android-sdk.zip

# Установите переменные окружения для Android SDK
ENV ANDROID_HOME /usr/local/android-sdk
ENV PATH $PATH:$ANDROID_HOME/tools/bin:$ANDROID_HOME/platform-tools

# Установите нужные компоненты Android SDK (поддержка для ARM и x86)
RUN yes | sdkmanager --licenses \
    && sdkmanager "platform-tools" \
    && sdkmanager "platforms;android-28" \
    && sdkmanager "build-tools;28.0.3" \
    && sdkmanager "extras;android;m2repository" \
    && sdkmanager "extras;google;m2repository"

# Копируйте файлы приложения в контейнер
WORKDIR /GraphEmail
COPY . .

# Соберите и запустите ваше Android-приложение
CMD ./gradlew assembleDebug && adb install -r GraphEmail/build/outputs/apk/debug/app-debug.apk
