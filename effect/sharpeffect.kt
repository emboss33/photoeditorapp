    fun applyDetailSharpening(src: Mat, sharpnessLevel: Double = 1.2): Mat {
        val tag = "SharpEffectProcessor"

        // 🔹 중간 결과 저장용 Mat들 선언
        val bgr = Mat()           // RGBA → BGR 변환된 이미지 (알파 제외)
        val blurred = Mat()       // 블러 처리된 BGR 이미지 (디테일 마스크 계산용)
        val detailMask = Mat()    // 원본과 블러 차이 → 디테일 강조 마스크
        val sharpened = Mat()     // 샤프닝 처리 결과 (BGR)
        val clipped: Mat          // RGB 값 클리핑된 결과 (샤프닝된 BGR → RGB 범위 보정)
        val result = Mat()        // 최종 RGBA 병합 이미지

        try {
            // 1. RGBA → BGR 변환 (OpenCV는 내부적으로 BGR 기준 처리)
            Imgproc.cvtColor(src, bgr, Imgproc.COLOR_RGBA2BGR)

            // 2. 부드럽게 블러 처리 (Unsharp Masking 기법의 기본)
            Imgproc.GaussianBlur(bgr, blurred, Size(0.0, 0.0), 0.1)

            // 3. 원본과 블러 이미지의 차이를 계산 → 디테일 강조 부분 추출
            Core.absdiff(bgr, blurred, detailMask)

            // 4. 원본 + 디테일 마스크를 가중합 → 샤프닝 처리
            Core.addWeighted(bgr, 1.0, detailMask, sharpnessLevel, 0.0, sharpened)

            // 5. 샤프닝 결과의 RGB 값을 0~255 범위로 보정
            clipped = clipRGBRange(sharpened)

            // 6. 원본 src에서 R, G, B, A 분리 → 알파 채널은 따로 유지
            val rgbaChannels = ArrayList<Mat>()
            Core.split(src, rgbaChannels)
            val alpha = rgbaChannels[3].clone() // 투명도 정보 따로 저장

            // 7. clipped에서 B, G, R 채널 추출
            val resultChannels = ArrayList<Mat>()
            Core.split(clipped, resultChannels)

            // 8. R, G, B + A 순으로 병합할 준비 (RGBA 순서)
            val reorderedChannels = listOf(
                resultChannels[2], // R
                resultChannels[1], // G
                resultChannels[0], // B
                alpha              // A
            )

            // 9. RGB + A 병합 → 최종 RGBA 이미지 구성
            Core.merge(reorderedChannels, result)

            // 🔹 더 이상 사용하지 않는 Mat 객체 해제
            alpha.release()
            rgbaChannels.forEach { it.release() }
            resultChannels.forEach { it.release() }

            return result
        } finally {
            // 🔹 예외 발생 여부와 관계없이 해제할 Mat
            bgr.release()
            blurred.release()
            detailMask.release()
            sharpened.release()

            // clipped는 위에서 복사한 값이고 반환에 포함되지 않으므로 release 생략 가능
        }
    } 
