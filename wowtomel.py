import os
import cv2
import numpy as np
import matplotlib.pyplot as plt
import librosa
import librosa.display
import pandas as pd
import warnings
import math  # math.ceil için

# Librosa'dan gelecek uyarıları kapatmak için (isteğe bağlı)
warnings.filterwarnings('ignore')

# --- Konfigürasyon Ayarları ---
data_dir = r'C:\Users\Bertan\Desktop\ses_klasoru'
save_dir = r'C:\Users\Bertan\Desktop\mel_analiz_yuksek_cozunurluk'  # Kayıt dizinini değiştirdim
max_files_per_subdir = 50  # Her alt dizinden işlenecek maksimum dosya sayısı (None = hepsi)

# Mel Spektrogram Parametreleri
n_mels = 128  # Mel bandı sayısı (dikey çözünürlük)
sr_target = 22050  # Hedef örnekleme hızı (tüm sesler buna yeniden örneklenir)
hop_length = 512  # FFT pencereleri arasındaki adım (zaman çözünürlüğü, küçük değer = daha fazla kare)
n_fft = 2048  # FFT pencere boyutu (frekans çözünürlüğü)

# Görselleştirme ve Kayıt Ayarları
# Kaydedilecek resimlerin piksel boyutları
image_output_pixels_square = (512, 512)  # Kare görseller için (Mel, STFT)
image_output_pixels_wide = (1024, 512)  # Geniş görseller için (HPSS, Chroma)
image_output_pixels_pitch = (1024, 256)  # Pitch grafiği için

output_dpi = 100  # Kaydedilecek resimlerin DPI'ı (piksel / inç). Daha yüksek değer = daha yüksek kalite.
# Örn: 512x512 piksel ve 100 DPI için figsize=(5.12, 5.12)
# Not: Matplotlib'in görüntüleme boyutu, figsize * dpi sonucudur.
# Buradaki amaç, savefig'in çıktısını istediğimiz piksel boyutunda almak.

# --- Veri Toplama ---
paths = []
labels = []
all_features = []  # Çıkarılan tüm sayısal özellikleri saklamak için liste

print(f"Ses dosyaları '{data_dir}' dizininden toplanıyor...")
for dirname, _, filenames in os.walk(data_dir):
    t = 0
    for filename in filenames:
        if filename.lower().endswith('.wav'):
            if max_files_per_subdir is None or t < max_files_per_subdir:
                full_path = os.path.join(dirname, filename)
                paths.append(full_path)
                labels.append(os.path.basename(dirname))
                t += 1
            else:
                break  # Belirli bir alt dizin için sınıra ulaşıldı

print(f"Toplam {len(paths)} adet .wav dosyası bulundu.")

# --- Kayıt Dizininin Oluşturulması ---
if not os.path.exists(save_dir):
    os.makedirs(save_dir)
    print(f"'{save_dir}' dizini oluşturuldu.")


# --- Detaylı Analiz Fonksiyonu ---
def analyze_and_save_audio_features(file_path, audio_label, file_name, save_base_dir):
    features = {
        'label': audio_label,
        'filename': file_name,
        'path': file_path,
        'duration_seconds': None,
        'mean_rms': None, 'std_rms': None,
        'mean_zcr': None, 'std_zcr': None,
        'mean_spectral_centroid': None, 'std_spectral_centroid': None,
        'mean_spectral_bandwidth': None, 'std_spectral_bandwidth': None,
        'mean_spectral_rolloff': None, 'std_spectral_rolloff': None,
        'mean_mfcc': None, 'std_mfcc': None,
        'mean_chroma_stft': None, 'std_chroma_stft': None,
        'peak_freq_overall_hz': None,
        'error_message': None  # Hata mesajını saklamak için
    }

    try:
        # Ses dosyasını yükle ve yeniden örnekle
        y, sr = librosa.load(file_path, sr=sr_target)
        features['duration_seconds'] = librosa.get_duration(y=y, sr=sr)

        # 1. Mel Spektrogramı
        mel_spectrogram = librosa.feature.melspectrogram(y=y, sr=sr, n_mels=n_mels, hop_length=hop_length, n_fft=n_fft)
        log_mel_spectrogram = librosa.power_to_db(mel_spectrogram, ref=np.max)

        # Görselleştirme için figure boyutunu ayarla
        fig_width_mel = image_output_pixels_square[0] / output_dpi
        fig_height_mel = image_output_pixels_square[1] / output_dpi
        plt.figure(figsize=(fig_width_mel, fig_height_mel), dpi=output_dpi)

        librosa.display.specshow(log_mel_spectrogram, sr=sr, x_axis='time', y_axis='mel', cmap='viridis',
                                 hop_length=hop_length)
        plt.colorbar(format='%+2.0f dB')
        plt.title(f'Mel Spektrogram: {file_name}')
        plt.tight_layout(pad=0)
        plt.savefig(os.path.join(save_base_dir, f'{audio_label}_{file_name}_mel_spec.png'), bbox_inches='tight',
                    pad_inches=0)
        plt.close()

        # 2. Kısa Zamanlı Fourier Dönüşümü (STFT) Spektrogramı
        stft = librosa.stft(y, n_fft=n_fft, hop_length=hop_length)
        stft_db = librosa.amplitude_to_db(np.abs(stft), ref=np.max)

        fig_width_stft = image_output_pixels_square[0] / output_dpi
        fig_height_stft = image_output_pixels_square[1] / output_dpi
        plt.figure(figsize=(fig_width_stft, fig_height_stft), dpi=output_dpi)

        librosa.display.specshow(stft_db, sr=sr, x_axis='time', y_axis='hz', cmap='magma', hop_length=hop_length)
        plt.colorbar(format='%+2.0f dB')
        plt.title(f'STFT Spektrogram: {file_name}')
        plt.tight_layout(pad=0)
        plt.savefig(os.path.join(save_base_dir, f'{audio_label}_{file_name}_stft_spec.png'), bbox_inches='tight',
                    pad_inches=0)
        plt.close()

        # 3. Harmonik ve Perküsif Bileşenlerin Ayrılması
        y_harmonic, y_percussive = librosa.effects.hpss(y)

        mel_spec_harmonic = librosa.feature.melspectrogram(y=y_harmonic, sr=sr, n_mels=n_mels, hop_length=hop_length,
                                                           n_fft=n_fft)
        log_mel_spec_harmonic = librosa.power_to_db(mel_spec_harmonic, ref=np.max)

        mel_spec_percussive = librosa.feature.melspectrogram(y=y_percussive, sr=sr, n_mels=n_mels,
                                                             hop_length=hop_length, n_fft=n_fft)
        log_mel_spec_percussive = librosa.power_to_db(mel_spec_percussive, ref=np.max)

        fig_width_hpss = image_output_pixels_wide[0] / output_dpi
        fig_height_hpss = image_output_pixels_wide[1] / output_dpi
        plt.figure(figsize=(fig_width_hpss, fig_height_hpss), dpi=output_dpi)  # İki görsel yan yana daha geniş olacak

        plt.subplot(1, 2, 1)
        librosa.display.specshow(log_mel_spec_harmonic, sr=sr, x_axis='time', y_axis='mel', cmap='Blues',
                                 hop_length=hop_length)
        plt.title('Harmonik Mel')
        plt.colorbar(format='%+2.0f dB')

        plt.subplot(1, 2, 2)
        librosa.display.specshow(log_mel_spec_percussive, sr=sr, x_axis='time', y_axis='mel', cmap='Reds',
                                 hop_length=hop_length)
        plt.title('Perküsif Mel')
        plt.colorbar(format='%+2.0f dB')
        plt.suptitle(f'Harmonik/Perküsif Mel Spektrogramları: {file_name}', y=1.02)  # Üst başlık
        plt.tight_layout(rect=[0, 0, 1, 0.95], pad=0)  # Başlık için yer bırak
        plt.savefig(os.path.join(save_base_dir, f'{audio_label}_{file_name}_hpss_mel_spec.png'), bbox_inches='tight',
                    pad_inches=0)
        plt.close()

        # 4. Temel Frekans (Pitch) Konturu
        # piptrack bazen boş sonuç dönebilir, kontrol edelim
        pitches, magnitudes = librosa.core.piptrack(y=y, sr=sr, hop_length=hop_length)

        # Sadece en yüksek magnitude'a sahip pitch'leri alalım ve sıfırları filtreleyelim
        pitch = []
        times = librosa.times_like(pitches, sr=sr, hop_length=hop_length)
        for t_idx in range(pitches.shape[1]):
            max_idx = np.argmax(magnitudes[:, t_idx])
            if pitches[max_idx, t_idx] > 0:  # Sadece geçerli pitch değerlerini al
                pitch.append(pitches[max_idx, t_idx])
            else:
                pitch.append(np.nan)  # Pitch yoksa NaN ekle
        pitch = np.array(pitch)

        fig_width_pitch = image_output_pixels_pitch[0] / output_dpi
        fig_height_pitch = image_output_pixels_pitch[1] / output_dpi
        plt.figure(figsize=(fig_width_pitch, fig_height_pitch), dpi=output_dpi)

        plt.plot(times[:len(pitch)], pitch, label='Estimated Pitch', linewidth=2, color='darkgreen')
        plt.title(f'Temel Frekans (Pitch) Konturu: {file_name}')
        plt.xlabel('Zaman (s)')
        plt.ylabel('Frekans (Hz)')
        plt.grid(True, linestyle=':', alpha=0.7)
        plt.ylim(bottom=0)  # Frekans 0'dan başlasın
        plt.tight_layout()
        plt.savefig(os.path.join(save_base_dir, f'{audio_label}_{file_name}_pitch.png'), bbox_inches='tight',
                    pad_inches=0)
        plt.close()

        # 5. Kromagram (Chroma Feature)
        chroma = librosa.feature.chroma_stft(y=y, sr=sr, n_fft=n_fft, hop_length=hop_length)

        # Kromagram görseli, zaman eksenindeki uzunluğuna göre dinamik olarak boyutlandırılmalı
        # Zaman boyutunu (sütun sayısı) alalım
        chroma_time_frames = chroma.shape[1]
        # Hedef piksel genişliğine göre en boy oranını koruyarak yüksekliği ayarla
        # Ancak chroma genelde 12 band içerir, bu yüzden yüksekliği sabitleyip genişliği ayarlamak daha mantıklı.

        # Kromagramın 12 bandı olduğu için dikey eksen sabittir.
        # Zaman ekseni sesin süresine bağlıdır.
        # Genişliği chroma_time_frames'e oranla daha büyük yapalım
        fig_width_chroma = (chroma_time_frames / output_dpi) * (
                    output_dpi / 20)  # Oran ayarlaması yapıldı, 20 keyfi bir sayı
        if fig_width_chroma < (image_output_pixels_wide[0] / output_dpi):  # Minimum genişlik
            fig_width_chroma = (image_output_pixels_wide[0] / output_dpi)
        fig_height_chroma = image_output_pixels_wide[1] / output_dpi  # Yüksekliği sabit tutalım

        plt.figure(figsize=(fig_width_chroma, fig_height_chroma), dpi=output_dpi)
        librosa.display.specshow(chroma, sr=sr, x_axis='time', y_axis='chroma', cmap='Greens', hop_length=hop_length)
        plt.colorbar()
        plt.title(f'Kromagram (Chroma): {file_name}')
        plt.tight_layout(pad=0)
        plt.savefig(os.path.join(save_base_dir, f'{audio_label}_{file_name}_chroma.png'), bbox_inches='tight',
                    pad_inches=0)
        plt.close()

        # --- Sayısal Özellik Çıkarımı ---
        # RMS (Root Mean Square) Enerjisi
        rms = librosa.feature.rms(y=y, hop_length=hop_length)
        features['mean_rms'] = np.mean(rms)
        features['std_rms'] = np.std(rms)

        # Zero Crossing Rate (ZCR)
        zcr = librosa.feature.zero_crossing_rate(y=y, hop_length=hop_length)
        features['mean_zcr'] = np.mean(zcr)
        features['std_zcr'] = np.std(zcr)

        # Spectral Centroid
        cent = librosa.feature.spectral_centroid(y=y, sr=sr, hop_length=hop_length)
        features['mean_spectral_centroid'] = np.mean(cent)
        features['std_spectral_centroid'] = np.std(cent)

        # Spectral Bandwidth
        bandwidth = librosa.feature.spectral_bandwidth(y=y, sr=sr, hop_length=hop_length)
        features['mean_spectral_bandwidth'] = np.mean(bandwidth)
        features['std_spectral_bandwidth'] = np.std(bandwidth)

        # Spectral Rolloff
        rolloff = librosa.feature.spectral_rolloff(y=y, sr=sr, hop_length=hop_length)
        features['mean_spectral_rolloff'] = np.mean(rolloff)
        features['std_spectral_rolloff'] = np.std(rolloff)

        # MFCC (Mel-frequency Cepstral Coefficients)
        mfccs = librosa.feature.mfcc(y=y, sr=sr, n_mfcc=13)  # Genellikle 13 MFCC kullanılır
        features['mean_mfcc'] = np.mean(mfccs, axis=1).tolist()  # Her bir MFCC katsayısının ortalaması
        features['std_mfcc'] = np.std(mfccs, axis=1).tolist()  # Her bir MFCC katsayısının standart sapması

        # Chroma STFT (ortalama)
        features['mean_chroma_stft'] = np.mean(chroma, axis=1).tolist()
        features['std_chroma_stft'] = np.std(chroma, axis=1).tolist()

        # Genel pik frekans (tüm spektrogramdaki en yüksek enerjili frekans)
        # stft_db boş değilse hesapla
        if stft_db.size > 0:
            peak_freq_overall = librosa.fft_frequencies(sr=sr, n_fft=n_fft)[np.argmax(stft_db)]
            features['peak_freq_overall_hz'] = peak_freq_overall

        all_features.append(features)
        print(f"Başarıyla işlendi: {file_path}")

    except Exception as e:
        print(f'HATA: {file_path} işlenirken bir sorun oluştu. Hata: {e}')
        features['error_message'] = str(e)
        all_features.append(features)


# --- Analiz Döngüsü ---
for i, path in enumerate(paths):
    label = os.path.basename(os.path.dirname(path))
    file = os.path.splitext(os.path.basename(path))[0]
    print(f"\n[{i + 1}/{len(paths)}] '{label}/{file}' işleniyor...")
    analyze_and_save_audio_features(path, label, file, save_dir)

# --- Çıkarılan Özellikleri Kaydetme ---
features_df = pd.DataFrame(all_features)
features_df.to_csv(os.path.join(save_dir, 'extracted_audio_features.csv'), index=False)
print(f"\nTüm çıkarılan özellikler '{os.path.join(save_dir, 'extracted_audio_features.csv')}' dosyasına kaydedildi.")
print("İşlem tamamlandı!")