import http from 'k6/http';
import {check} from 'k6';

export const options = {
    vus: 100,
    iterations: 100,
};

export default function () {
    const res = http.patch('http://localhost:8080/coupons/1/decrease');

    check(res, {
        'status is 200': (r) => r.status === 200,
    });
}