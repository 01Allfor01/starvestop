declare global {
    interface Window {
        TossPayments: (clientKey: string) => {
            payment: (options: { customerKey: string }) => {
                requestBillingAuth: (options: {
                    method: 'CARD';
                    successUrl: string;
                    failUrl: string;
                    customerEmail: string;
                    customerName: string;
                }) => Promise<void>;
            };
        };
    }
}

export {};