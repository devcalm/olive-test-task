import http from 'k6/http';
import {sleep, check} from 'k6';

const host = __ENV.SERVER_HOST || 'localhost';
const port = __ENV.SERVER_PORT || '8080';

export const options = {
    vus: 10, // 10 virtual users
    duration: '30s', // Run the test for 30 seconds
};

const headers = {
    'Content-Type': 'application/json',
};

const url = `http://${host}:${port}/api/detection/batch`;

export default function () {
    while (true) {
        const payloads = generateMessages();

        const response = http.post(url, JSON.stringify(payloads), {headers});

        check(response, {
            'status is 200': (r) => r.status === 200,
        });

        sleep(0.1); // 100ms = 0.1 seconds
    }
}

function generateMessages() {
    const numDetections = generateRandomDetections()
    const detections = [];

    for (let i = 0; i < numDetections; i++) {
        detections.push(generateDetection());
    }
    return detections;
}

function generateRandomDetections() {
    return Math.floor(Math.random() * 101);
}

function generateDetection() {
    return {
        guid: generateUUID(),
        location: {
            latitude: (Math.random() * 180 - 90).toFixed(4),
            longitude: (Math.random() * 360 - 180).toFixed(4),
        },
        source: `device-${Math.floor(Math.random() * 1000) + 1}`,
        type: ['CAR', 'MOTORBIKE', 'BICYCLE', 'SKATEBOARD'][Math.floor(Math.random() * 4)],
        confidence: (Math.random() * 0.5 + 0.5).toFixed(2),
        date: new Date().toISOString(),
    };
}

function generateUUID() {
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
        const r = (Math.random() * 16) | 0;
        const v = c === 'x' ? r : (r & 0x3) | 0x8;
        return v.toString(16);
    });
}

