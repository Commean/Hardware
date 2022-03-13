#include <lmic.h>
#include <hal/hal.h>

// Pin mapping
const lmic_pinmap lmic_pins = {
    .nss = 18,
    .rxtx = LMIC_UNUSED_PIN,
    .rst = 14,
    // LoRa mode
    // DIO0: TxDone and RxDone
    // DIO1: RxTimeout

    // FSK mode
    // DIO0: PayloadReady and PacketSent
    // DIO2: TimeOut
    .dio = {26, 35, LMIC_UNUSED_PIN},
};
